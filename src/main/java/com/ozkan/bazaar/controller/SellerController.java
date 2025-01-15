package com.ozkan.bazaar.controller;

import com.ozkan.bazaar.model.VerificationCode;
import com.ozkan.bazaar.repository.IVerificationCodeRepository;
import com.ozkan.bazaar.request.LoginRequest;
import com.ozkan.bazaar.response.ApiResponse;
import com.ozkan.bazaar.response.AuthResponse;
import com.ozkan.bazaar.service.IAuthService;
import com.ozkan.bazaar.service.ISellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {

    private final ISellerService service;
    private final IVerificationCodeRepository verificationCodeRepository;
    private final IAuthService authService;


//    @PostMapping("/sent/login-otp")
//    public ResponseEntity<ApiResponse> sentOtpHandler(
//            @RequestBody VerificationCode req) throws  Exception{
//
//        authService.sentLoginOtp(req.getEmail());
//        ApiResponse res = new ApiResponse();
//        res.setMessage("otp sent successfully");
//
//        return ResponseEntity.ok(res);
//
//
//    }



    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest req) throws Exception {

        String otp = req.getOtp();
        String email = req.getEmail();


        req.setEmail("seller_"+email);
        AuthResponse authResponse = authService.signin(req);

        return ResponseEntity.ok(authResponse);
    }
}
