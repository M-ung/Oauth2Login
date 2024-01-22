package com.example.security1.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.security.Provider;
import java.security.Timestamp;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role; //ROLE_USER, ROLE_ADMIN

    private String provider;
    private String providerId;
}
