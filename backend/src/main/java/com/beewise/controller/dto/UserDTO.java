package com.beewise.controller.dto;

import com.beewise.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String username;
    private int points;
    private List<Long> completedLessons;
    private int currentLesson;

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.points = user.getPoints();
        this.completedLessons = user.getLessonProgresses()
                .stream()
                .map(p -> p.getLesson().getId())
                .toList();
        this.currentLesson = user.getCurrentLesson();
    }
}
