package org.example.fintect.auth;

import lombok.RequiredArgsConstructor;
import org.example.fintect.auth.model.AuthResponse;


import org.example.fintect.auth.model.SignInRequest;
import org.example.fintect.exceptions.InvalidDataException;
import org.example.fintect.response.ApiResponse;
import org.example.fintect.service.JwtService;
import org.example.fintect.user.Role;
import org.example.fintect.user.User;
import org.example.fintect.user.UsersRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    private static final long ACCESS_TOKEN_EXP = 15 * 60 * 1000; // 15 min
    private static final long REFRESH_TOKEN_EXP = 7 * 24 * 60 * 60 * 1000; // 7 days


    public AuthResponse signUpAsAdmin(RegisterRequest registerRequest) {
        User existingUser = usersRepository.findByEmail(registerRequest.email()).orElse(null);
        if (existingUser != null) {
            throw new InvalidDataException("Email already used. Please try another one.");
        }

        User user = new User();
        user.setEmail(registerRequest.email());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setUsername(registerRequest.email());
        user.setName(registerRequest.name());
        user.setContactNumber(registerRequest.contactNumber());
        user.setRole(Role.ADMIN);
        usersRepository.save(user);
        return generateTokenFromHere(user);

    }


    public AuthResponse signUpAsStaff(RegisterRequest registerRequest) {
        User existingUser = usersRepository.findByEmail(registerRequest.email()).orElse(null);
        if (existingUser != null) {
            throw new InvalidDataException("Email already used. Please try another one.");
        }

        User user = new User();
        user.setEmail(registerRequest.email());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setUsername(registerRequest.email());
        user.setRole(Role.FINANCE_STAFF);
        usersRepository.save(user);
        return generateTokenFromHere(user);

    }



    public AuthResponse signUpCustomer(RegisterRequest registerRequest) {
        User existingUser = usersRepository.findByEmail(registerRequest.email()).orElse(null);
        if (existingUser != null) {
            throw new InvalidDataException("Email already used. Please try another one.");
        }

        User user = new User();
        user.setEmail(registerRequest.email());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setUsername(registerRequest.email());
        user.setRole(Role.CUSTOMER);
        usersRepository.save(user);
        return generateTokenFromHere(user);

    }



    public AuthResponse signInAllUsers(SignInRequest signInRequest) {

        Optional<User> user = usersRepository.findByEmail(signInRequest.email());
        if (user.isEmpty()) {
            throw new InvalidDataException("Please Provide a valid email address.");
        }
        if(!passwordEncoder.matches(signInRequest.password(),user.get().getPassword())) {
            throw new BadCredentialsException("Invalid password.Please try again");
        }
        return generateTokenFromHere(user.get());
    }













    private AuthResponse generateTokenFromHere(User user) {
        String accessToken = jwtService.generateToken(user);
//        String refreshToken = jwtService.generateToken(user);
        return new AuthResponse(accessToken);
    }
}
