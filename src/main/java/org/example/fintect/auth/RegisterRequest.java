package org.example.fintect.auth;

import org.example.fintect.user.Role;

public record RegisterRequest(

        String name,
        String email,
        String password,
        String contactNumber,
        Role role
) {
}
