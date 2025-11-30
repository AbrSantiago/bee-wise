package com.beewise.model;

import lombok.Getter;

@Getter
public enum ExerciseCategory {
    MATRICES("Matrices"),
    DETERMINANTS("Determinantes"),
    SYSTEM_OF_EQUATIONS("Sistemas de ecuaciones"),
    GROUP_THEORY("Teoría de grupos"),
    VECTOR_SPACES("Espacios vectoriales"),
    DIVISIBILITY("Divisibilidad");

    private final String displayName;

    ExerciseCategory(String displayName) {
        this.displayName = displayName;
    }

}
