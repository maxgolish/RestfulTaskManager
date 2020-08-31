package com.example.entity;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.example.web.TaskController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
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