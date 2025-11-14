package br.com.deolhonacamara.api.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserEntity {
    private UUID id;
    private String fullName;
    private String email;
    private String password;
    private boolean active;
    private LocalDateTime createdAt;
}