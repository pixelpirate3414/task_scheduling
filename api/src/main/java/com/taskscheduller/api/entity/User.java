package com.taskscheduller.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class User {

	@Id
	@SequenceGenerator(
			name = "user_seq",
			sequenceName = "user_sequence",
			allocationSize = 50
	)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
	private Long id;

	@Column(nullable = false, unique = true)
	@NotBlank
	@Email
	@Size(max = 255)
	private String email;

	@Column(nullable = false)
	@NotBlank
	private String password;

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	@OneToMany(
			mappedBy = "owner",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	private List<Task> tasks = new ArrayList<>();
}
