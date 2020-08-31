package com.example.config;

import com.example.entity.Task;
import com.example.entity.TaskProgress;
import com.example.repo.TaskRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class LoadDatabase {
    private static final Logger logger = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(TaskRepository taskRepository) {
        return args -> {
            taskRepository.save(new Task("Add Lombok", TaskProgress.IN_PROGRESS));
            taskRepository.save(new Task("Find job", TaskProgress.IN_PROGRESS));
            taskRepository.save(new Task("Download idea", TaskProgress.DONE));
            taskRepository.findAll().forEach(task -> logger.info("Preloaded " + task));
        };
    }
}