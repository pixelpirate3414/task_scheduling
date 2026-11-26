package com.taskscheduler.api.integration.repository;

import com.taskscheduler.api.entity.User;
import com.taskscheduler.api.integration.TestcontainersConfiguration;
import com.taskscheduler.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
class UserRepositoryTest {
    @Autowired UserRepository userRepository;

    @Test
    void findByEmailReturnsMatchingUser() {
        userRepository.saveAndFlush(new User("person@example.com", "encoded-password"));

        var result = userRepository.findByEmail("person@example.com");

        assertThat(result).isPresent();
        assertThat(result.orElseThrow().getEmail()).isEqualTo("person@example.com");
        assertThat(result.orElseThrow().getPassword()).isEqualTo("encoded-password");
    }

    @Test
    void findByEmailReturnsEmptyWhenEmailIsUnknown() {
        assertThat(userRepository.findByEmail("missing@example.com")).isEmpty();
    }
}
