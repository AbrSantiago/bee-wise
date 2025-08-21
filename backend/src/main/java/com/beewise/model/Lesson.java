package com.beewise.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exercise> exercises;

    public Lesson(){
        this.exercises = new ArrayList<>();
    }

    public Lesson(List<Exercise> exercises){
        this.exercises = exercises;
    }
}
