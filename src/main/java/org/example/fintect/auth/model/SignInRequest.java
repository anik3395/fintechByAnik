package org.example.fintect.auth.model;

public record SignInRequest(
        String email,
        String password
) {}
