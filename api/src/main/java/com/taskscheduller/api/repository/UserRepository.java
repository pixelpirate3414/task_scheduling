package com.taskscheduller.api.repository;

import com.taskscheduller.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
