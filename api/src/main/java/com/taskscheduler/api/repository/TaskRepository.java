package com.taskscheduler.api.repository;

import com.taskscheduler.api.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
	Optional<Task> findByIdAndOwnerEmail(Long id, String ownerEmail);
}
