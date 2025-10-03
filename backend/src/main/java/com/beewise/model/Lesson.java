package com.beewise.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 255, message = "Title too long")
    private String title;

    @Size(max = 1000, message = "Description too long")
    private String description;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<Exercise> exercises;

    public Lesson() {}

    public Lesson(String title, String description){
        this.title = title;
        this.description = description;
        this.exercises = new ArrayList<>();
    }

    public Lesson(String title, String description, List<Exercise> exercises){
        this.title = title;
        this.description = description;
        this.exercises = exercises;
    }
}
