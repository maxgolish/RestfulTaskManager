package com.example.service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.example.model.Task;
import com.example.model.TaskProgress;
import com.example.web.TaskController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Service;

@Service
public class TaskModelAssembler implements RepresentationModelAssembler<Task, EntityModel<Task>> {

    @Override
    public EntityModel<Task> toModel(Task task) {
        EntityModel<Task> taskModel = EntityModel.of(task,
                linkTo(methodOn(TaskController.class).one(task.getId())).withSelfRel(),
                linkTo(methodOn(TaskController.class).all()).withRel("tasks"));
        if(task.getProgress() == TaskProgress.IN_PROGRESS) {
            taskModel.add(linkTo(methodOn(TaskController.class).complete(task.getId()))
                    .withRel("complete"));
        }
        return taskModel;
    }
}