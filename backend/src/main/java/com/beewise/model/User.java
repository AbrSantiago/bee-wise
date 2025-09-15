package com.beewise.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be empty")
    @Size(max = 50, message = "Name is too long")
    private String name;

    @NotBlank(message = "Surname cannot be empty")
    @Size(max = 50, message = "Surname is too long")
    private String surname;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email is not valid")
    @Size(max = 100, message = "Email is too long")
    @Column(unique = true, length = 150)
    private String email;

    @NotBlank(message = "Username cannot be empty")
    @Size(max = 30, message = "Username is too long")
    @Column(unique = true, length = 30)
    private String username;

    @Column(nullable = false, length = 255)
    private String passwordHash; // Store the hash, not the plain password

    @Min(value = 0, message = "Points cannot be negative")
    private Integer points = 0;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LessonProgress> lessonProgresses = new ArrayList<>();

    public User(){}

    public User(String name, String surname, String email, String username, String passwordHash, Integer points, List<LessonProgress> lessonProgresses) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.username = username;
        this.passwordHash = passwordHash;
        this.points = points;
        this.lessonProgresses = lessonProgresses;
    }
}
