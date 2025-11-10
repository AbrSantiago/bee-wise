package com.beewise.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Level {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private int minPoints;

    private String iconUrl;

    public Level(Long id, String name, int minPoints, String iconUrl) {
        this.id = id;
        this.name = name;
        this.minPoints = minPoints;
        this.iconUrl = iconUrl;
    }
}