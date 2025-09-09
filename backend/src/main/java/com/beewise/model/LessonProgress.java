package com.beewise.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "lesson_progress")
public class LessonProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Min(0)
    private Integer score = 0;

    @Min(0)
    @Max(100)
    private Double percentCompleted = 0.0;

    private Boolean completed = false;

    @Min(0)
    private Integer attemps = 0;

    protected LessonProgress() {
    }

    public LessonProgress(User user, Lesson lesson, Integer score, Double percentCompleted, Boolean completed, Integer attemps) {
        this.user = user;
        this.lesson = lesson;
        this.score = score;
        this.percentCompleted = percentCompleted;
        this.completed = completed;
        this.attemps = attemps;
    }
}
