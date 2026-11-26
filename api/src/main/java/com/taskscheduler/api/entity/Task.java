package com.taskscheduler.api.entity;

import com.taskscheduler.api.exception.InvalidTaskStateException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "tasks")
@Getter
@NoArgsConstructor
public class Task {

	@Id
	@SequenceGenerator(
			name = "task_seq",
			sequenceName = "task_sequence",
			allocationSize = 50
    )
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "task_seq")
	private Long id;

    @Column(nullable = false, length = 255)
	@Size(max = 255)
	@NotBlank
	private String title;

    @Column(length = 2048)
	@Size(max = 2048)
	private String body;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "owner_id", nullable = false)
	@NotNull
	private User owner;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@NotNull
	private TaskStatus status;

	@Column(nullable = false, updatable = false)
	@NotNull
	private Instant createdAt;

	private Instant doneAt;

	public Task(String title, String body, User owner) {
		this.title = Objects.requireNonNull(title);
		this.body = body;
		this.owner = Objects.requireNonNull(owner);
		this.status = TaskStatus.IN_PROGRESS;
	}

	@PrePersist
	protected void onCreate() {
		if (this.status == null) {
			this.status = TaskStatus.IN_PROGRESS;
		}

		if (this.createdAt == null) {
			this.createdAt = Instant.now();
		}
	}

	public void complete() {
		if (this.status == TaskStatus.DONE) {
			throw new InvalidTaskStateException("Task has already been completed");
		}

		this.status = TaskStatus.DONE;
		this.doneAt = Instant.now();
	}

	public void reopen() {
		if (this.status != TaskStatus.DONE) {
			throw new InvalidTaskStateException("Cannot reopen task");
		}

		this.status = TaskStatus.IN_PROGRESS;
		this.doneAt = null;
	}
}
