package com.example.service;

import com.example.model.Task;
import com.example.model.TaskProgress;
import com.example.repo.TaskRepository;
import com.example.web.TaskController;
import com.example.web.exception.TaskNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class TaskService {
    @Autowired
    private final TaskRepository taskRepository;

    @Autowired
    private final TaskModelAssembler taskModelAssembler;

    public TaskService(TaskRepository taskRepository, TaskModelAssembler taskModelAssembler) {
        this.taskRepository = taskRepository;
        this.taskModelAssembler = taskModelAssembler;
    }

    public CollectionModel<EntityModel<Task>> getAll() {
        List<EntityModel<Task>> orders = taskRepository.findAll().stream() //
                .map(taskModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(orders,
                linkTo(methodOn(TaskController.class).all()).withSelfRel());
    }

    public EntityModel<Task> getOneById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        return taskModelAssembler.toModel(task);
    }

    public CollectionModel<EntityModel<Task>> getCurrent() {
        List<EntityModel<Task>> orders = taskRepository
                .findAllByProgress(TaskProgress.IN_PROGRESS)
                .stream()
                .map(taskModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(orders,
                linkTo(methodOn(TaskController.class).current()).withSelfRel());
    }

    public CollectionModel<EntityModel<Task>> getDone() {
        List<EntityModel<Task>> orders = taskRepository
                .findAllByProgress(TaskProgress.DONE)
                .stream()
                .map(taskModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(orders,
                linkTo(methodOn(TaskController.class).current()).withSelfRel());
    }

    public EntityModel<Task> newTask(Task task) {
        task.setProgress(TaskProgress.IN_PROGRESS);

        return taskModelAssembler.toModel(taskRepository.save(task));
    }

    public ResponseEntity<?> completeTask(Long id) {
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

    public EntityModel<Task> deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        taskRepository.deleteById(id);
        return taskModelAssembler.toModel(task);
    }
}
