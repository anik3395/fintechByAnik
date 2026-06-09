package org.example.fintect.encryptdecrypt.eesponsemodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long id;

    private String username;

    private String email;

    private String phoneNumber;
}
