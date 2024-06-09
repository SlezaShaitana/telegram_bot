package com.skillbox.cryptobot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Table(name = "subscribers")
@Entity
public class Subscribers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "UUID", nullable = false) //userId
    private UUID userId;
    @Column(name = "user_id", nullable = false)
    private Long telegramId;
    @Column(name = "price", nullable = true)
    private Double price;

}
