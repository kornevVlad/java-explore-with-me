package ru.practicum.model_package.user.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name; //имя пользователя

    @Column(name = "email")
    private String email; //email пользователя
}