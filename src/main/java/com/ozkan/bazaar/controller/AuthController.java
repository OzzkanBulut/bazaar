package com.ozkan.bazaar.controller;

import com.ozkan.bazaar.domain.USER_ROLE;
import com.ozkan.bazaar.model.User;
import com.ozkan.bazaar.model.VerificationCode;
import com.ozkan.bazaar.repository.IUserRepository;
import com.ozkan.bazaar.request.LoginOtpRequest;
import com.ozkan.bazaar.request.LoginRequest;
import com.ozkan.bazaar.response.ApiResponse;
import com.ozkan.bazaar.response.AuthResponse;
import com.ozkan.bazaar.response.SignUpRequest;
import com.ozkan.bazaar.service.impl.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IUserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignUpRequest request) throws Exception {

        String jwt = authService.createUser(request);

        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setMessage("Register success");
        response.setRole(USER_ROLE.ROLE_CUSTOMER);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/sent/login-signup-otp")
    public ResponseEntity<ApiResponse> sentOtpHandler(@RequestBody LoginOtpRequest request) throws Exception {

        authService.sentLoginOtp(request.getEmail(), request.getRole());

        ApiResponse response = new ApiResponse();
        response.setMessage("Otp Sent Successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest loginRequest) throws Exception {

        AuthResponse authResponse = authService.signin(loginRequest);

        return ResponseEntity.ok(authResponse);
    }
}
