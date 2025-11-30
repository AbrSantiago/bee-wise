package com.beewise.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "avatars")
@NoArgsConstructor
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "hair_id")
    private ShopItem hair;

    @ManyToOne
    @JoinColumn(name = "shirt_id")
    private ShopItem shirt;

    @ManyToOne
    @JoinColumn(name = "skin_id")
    private ShopItem skin;

    @ManyToOne
    @JoinColumn(name = "background_id")
    private ShopItem background;
}
