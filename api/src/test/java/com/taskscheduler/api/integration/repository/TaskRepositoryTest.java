package com.taskscheduler.api.integration.repository;

import com.taskscheduler.api.entity.Task;
import com.taskscheduler.api.entity.User;
import com.taskscheduler.api.integration.TestcontainersConfiguration;
import com.taskscheduler.api.repository.TaskRepository;
import com.taskscheduler.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
class TaskRepositoryTest {
    @Autowired TaskRepository taskRepository;
    @Autowired UserRepository userRepository;

    @Test
    void findByIdAndOwnerEmailReturnsTaskForMatchingOwner() {
        var owner = userRepository.saveAndFlush(new User("owner@example.com", "encoded"));
        var otherOwner = userRepository.saveAndFlush(new User("other@example.com", "encoded"));
        var task = taskRepository.saveAndFlush(new Task("owned task", "details", owner));

        var result = taskRepository.findByIdAndOwnerEmail(task.getId(), "owner@example.com");

        assertThat(result).isPresent();
        assertThat(result.orElseThrow().getTitle()).isEqualTo("owned task");
        assertThat(result.orElseThrow().getOwner().getEmail()).isEqualTo("owner@example.com");
        assertThat(taskRepository.findByIdAndOwnerEmail(task.getId(), otherOwner.getEmail())).isEmpty();
    }

    @Test
    void findByIdAndOwnerEmailReturnsEmptyForUnknownTask() {
        assertThat(taskRepository.findByIdAndOwnerEmail(999L, "owner@example.com")).isEmpty();
    }
}
