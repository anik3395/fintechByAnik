package org.example.fintect.encryptdecrypt.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserED {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String addresses;
}
