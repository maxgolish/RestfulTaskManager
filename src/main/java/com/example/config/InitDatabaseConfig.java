package com.example.config;

import com.example.model.Task;
import com.example.model.TaskProgress;
import com.example.repo.TaskRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;


@Configuration
public class InitDatabaseConfig implements InitializingBean {
    @Autowired
    private final TaskRepository taskRepository;
    private static final Logger logger = LoggerFactory.getLogger(InitDatabaseConfig.class);

    public InitDatabaseConfig(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        taskRepository.save(new Task("Add Lombok", TaskProgress.IN_PROGRESS));
        taskRepository.save(new Task("Find job", TaskProgress.IN_PROGRESS));
        taskRepository.save(new Task("Download idea", TaskProgress.DONE));
        taskRepository.findAll().forEach(task -> logger.info("Preloaded " + task));
    }
}