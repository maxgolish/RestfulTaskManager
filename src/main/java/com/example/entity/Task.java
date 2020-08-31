package com.example.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String description;

    private TaskProgress progress;

    public Task() {
    }

    public Task(String description) {
        this.description = description;
        progress = TaskProgress.IN_PROGRESS;
    }

    public Task(String description, TaskProgress progress) {
        this.description = description;
        this.progress = progress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskProgress getProgress() {
        return progress;
    }

    public void setProgress(TaskProgress taskProgress) {
        this.progress = taskProgress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id.equals(task.id) &&
                description.equals(task.description) &&
                progress == task.progress;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, progress);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", taskProgress=" + progress +
                '}';
    }
}
