package com.example.repo;

import com.example.entity.Task;
import com.example.entity.TaskProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByProgress(TaskProgress progress);
}