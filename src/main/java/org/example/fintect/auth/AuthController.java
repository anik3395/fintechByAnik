package org.example.fintect.auth;


import lombok.RequiredArgsConstructor;
import org.example.fintect.auth.model.AuthResponse;


import org.example.fintect.auth.model.SignInRequest;
import org.example.fintect.response.ApiResponse;
import org.example.fintect.utils.EndPointUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(EndPointUtils.ADMIN_SIGNUP)
    public ResponseEntity<ApiResponse<AuthResponse>> signUpAsAdmin(@RequestBody RegisterRequest registerRequest) {
        AuthResponse response = authService.signUpAsAdmin(registerRequest);
        return ResponseEntity.ok(ApiResponse.success("Admin successfully registered",response));
    }


    @PostMapping(EndPointUtils.FINANCE_STAFF_SIGNUP)
    public ResponseEntity<ApiResponse<AuthResponse>> signUpAsStaff(@RequestBody RegisterRequest registerRequest) {
        AuthResponse response = authService.signUpAsStaff(registerRequest);
        return ResponseEntity.ok(ApiResponse.success("Finance Staff successfully registered",response));
    }


    @PostMapping(EndPointUtils.CUSTOMER_SIGNUP)
    public ResponseEntity<ApiResponse<AuthResponse>> signUpCustomer(@RequestBody RegisterRequest registerRequest) {
        AuthResponse response = authService.signUpCustomer(registerRequest);
        return ResponseEntity.ok(ApiResponse.success("Customer successfully registered",response));
    }


    @PostMapping(value = EndPointUtils.USERS_SIGN_IN)
    public ResponseEntity<ApiResponse<AuthResponse>> signInAllUsers(@RequestBody SignInRequest signInRequest) {
        AuthResponse response = authService.signInAllUsers(signInRequest);
        return ResponseEntity.ok(ApiResponse.success("Users SignIn successfully done",response));
    }



}
