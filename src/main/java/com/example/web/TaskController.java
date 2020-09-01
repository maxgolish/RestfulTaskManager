package com.example.web;

import com.example.model.Task;
import com.example.service.TaskService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public CollectionModel<EntityModel<Task>> all() {
        return taskService.getAll();
    }

    @GetMapping("/{id}")
    public EntityModel<Task> one(@PathVariable Long id) {
        return taskService.getOneById(id);
    }

    @GetMapping("/current")
    public CollectionModel<EntityModel<Task>> current() {
        return taskService.getCurrent();
    }

    @GetMapping("/done")
    public CollectionModel<EntityModel<Task>> done() {
        return taskService.getDone();
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public EntityModel<Task> newTask(@RequestBody Task task) {
        return taskService.newTask(task);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<?> complete(@PathVariable Long id) {
        return taskService.completeTask(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public EntityModel<Task> delete(@PathVariable Long id) {
        return taskService.deleteTask(id);
    }
}