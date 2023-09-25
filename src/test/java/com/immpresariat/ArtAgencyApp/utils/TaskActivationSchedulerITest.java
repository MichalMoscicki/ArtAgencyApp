package com.immpresariat.ArtAgencyApp.utils;

import com.immpresariat.ArtAgencyApp.models.Task;
import com.immpresariat.ArtAgencyApp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TaskActivationSchedulerITest {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskActivationScheduler taskScheduler;

    @BeforeEach
    public void setup() {
        taskRepository.deleteAll();
    }

    @Test
    public void testCheckAndSetTaskActivationStatus() {
        LocalDate currentDate = LocalDate.now();
        Task task = Task.builder()
                .title("Test Task")
                .active(false)
                .priority(1)
                .activationDate(currentDate)
                .build();
        taskRepository.save(task);

        Task task2 = Task.builder()
                .title("Test Task2")
                .active(false)
                .priority(1)
                .activationDate(currentDate.plusDays(1))
                .build();
        taskRepository.save(task2);



        taskScheduler.checkAndSetTaskActivationStatus();

        Task updatedTask = taskRepository.findById(task.getId()).orElse(null);
        assert updatedTask != null;
        assertTrue(updatedTask.isActive());

        Task notUpdatedTask = taskRepository.findById(task2.getId()).orElse(null);
        assert notUpdatedTask != null;
        assertFalse(notUpdatedTask.isActive());
    }



}

