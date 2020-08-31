package com.example.web;

import com.example.entity.Task;
import com.example.entity.TaskModelAssembler;
import com.example.entity.TaskProgress;
import com.example.repo.TaskRepository;
import com.example.web.exception.TaskNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskRepository taskRepository;
    private final TaskModelAssembler taskModelAssembler;

    public TaskController(TaskRepository taskRepository, TaskModelAssembler taskModelAssembler) {
        this.taskRepository = taskRepository;
        this.taskModelAssembler = taskModelAssembler;
    }

    @GetMapping("/")
    public CollectionModel<EntityModel<Task>> all() {
        List<EntityModel<Task>> orders = taskRepository.findAll().stream() //
                .map(taskModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(orders,
                linkTo(methodOn(TaskController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Task> one(@PathVariable Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        return taskModelAssembler.toModel(task);
    }

    @GetMapping("/current")
    public CollectionModel<EntityModel<Task>> current() {
        List<EntityModel<Task>> orders = taskRepository
                .findAllByProgress(TaskProgress.IN_PROGRESS)
                .stream()
                .map(taskModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(orders,
                linkTo(methodOn(TaskController.class).current()).withSelfRel());
    }

    @GetMapping("/done")
    public CollectionModel<EntityModel<Task>> done() {
        List<EntityModel<Task>> orders = taskRepository
                .findAllByProgress(TaskProgress.DONE)
                .stream()
                .map(taskModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(orders,
                linkTo(methodOn(TaskController.class).current()).withSelfRel());
    }

    @PostMapping("/")
    public ResponseEntity<EntityModel<Task>> newTask(@RequestBody Task task) {

        task.setProgress(TaskProgress.IN_PROGRESS);
        Task newTask = taskRepository.save(task);

        return ResponseEntity
                .created(linkTo(methodOn(TaskController.class).one(newTask.getId())).toUri())
                .body(taskModelAssembler.toModel(newTask));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<?> complete(@PathVariable Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        if(task.getProgress() == TaskProgress.IN_PROGRESS) {
            task.setProgress(TaskProgress.DONE);
            return ResponseEntity.ok(taskModelAssembler.toModel(taskRepository.save(task)));
        }

        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .header(HttpHeaders.CONTENT_TYPE,
                        MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
                .body(Problem.create()
                        .withTitle("Method not allowed")
                        .withDetail("You can't do a task that is in the " +
                                task.getProgress() + " status"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<EntityModel<Task>> delete(@PathVariable Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        taskRepository.deleteById(id);
        return ResponseEntity.ok(taskModelAssembler.toModel(task));
    }
}