package com.immpresariat.ArtAgencyApp.utils;

import com.immpresariat.ArtAgencyApp.models.Task;
import com.immpresariat.ArtAgencyApp.repository.TaskRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TaskActivationScheduler {
    private final TaskRepository taskRepository;

    public TaskActivationScheduler(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkAndSetTaskActivationStatus() {
        LocalDate currentDate = LocalDate.now();

        List<Task> tasksToCheck = taskRepository.findByActivationDate(currentDate);

        for (Task task : tasksToCheck) {
            if (currentDate.equals(task.getActivationDate())) {
                task.setActive(true);
                taskRepository.save(task);
            }
        }
    }
}


