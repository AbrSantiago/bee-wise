package com.beewise.model;

import com.beewise.exception.NotEnoughBeeCoinsException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
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

    @Column(nullable = false)
    private String passwordHash; // Store the hash, not the plain password

    @Min(value = 0, message = "Points cannot be negative")
    private Integer points = 0;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LessonProgress> lessonProgresses = new ArrayList<>();

    @Min(value = 1, message = "Current lesson cannot be negative")
    private int currentLesson = 1;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Avatar avatar;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "level_id")
    private Level level;

    @ManyToMany
    @JoinTable(
            name = "user_items",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<ShopItem> items = new ArrayList<>();

    @Min(value = 0, message = "BeeCoins cannot be negative")
    private int beeCoins = 100;

    private int spentBeeCoins = 0;

    public void addPoints(int delta) {
        this.points += delta;
    }

    public void addBeeCoins(int delta) {
        this.beeCoins += delta;
    }

    public void addItem(ShopItem item) {
        this.items.add(item);
    }

    public void spendBeeCoins(int delta) {
        if (this.beeCoins < delta) {
            throw new NotEnoughBeeCoinsException("User " + username + " has no enough bee coims");
        }
        this.beeCoins -= delta;
        this.spentBeeCoins += delta;
    }
}
