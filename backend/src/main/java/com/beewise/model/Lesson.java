package com.beewise.model;

import jakarta.persistence.*;
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

    private String title;

    private String description;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<Exercise> exercises;

    protected Lesson() {}

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
