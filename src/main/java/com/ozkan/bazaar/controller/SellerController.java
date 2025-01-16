package com.ozkan.bazaar.controller;

import com.ozkan.bazaar.config.JwtProvider;
import com.ozkan.bazaar.domain.AccountStatus;
import com.ozkan.bazaar.model.Seller;
import com.ozkan.bazaar.model.VerificationCode;
import com.ozkan.bazaar.repository.IVerificationCodeRepository;
import com.ozkan.bazaar.request.LoginRequest;
import com.ozkan.bazaar.response.ApiResponse;
import com.ozkan.bazaar.response.AuthResponse;
import com.ozkan.bazaar.service.IAuthService;
import com.ozkan.bazaar.service.ISellerService;
import com.ozkan.bazaar.service.impl.EmailService;
import com.ozkan.bazaar.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {

    private final ISellerService sellerService;
    private final IVerificationCodeRepository verificationCodeRepository;
    private final IAuthService authService;
    private final EmailService emailService;
    private final JwtProvider jwtProvider;



    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest req) throws Exception {

        String otp = req.getOtp();
        String email = req.getEmail();


        req.setEmail("seller_"+email);
        AuthResponse authResponse = authService.signin(req);

        System.out.println("EMAIL -->"+email+"|OTP --->"+otp);

        return ResponseEntity.ok(authResponse);
    }

    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(
            @PathVariable String otp) throws Exception{

        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);

        if(verificationCode == null || !verificationCode.getOtp().equals(otp)){
            throw new Exception("Wrong otp");
        }

        Seller seller =sellerService.verifyEmail(verificationCode.getEmail(),otp);

        return new ResponseEntity<>(seller, HttpStatus.OK);


    }

    @PostMapping
    public ResponseEntity<Seller> createSeller(
            @RequestBody Seller seller) throws  Exception{

        Seller savedSeller = sellerService.createSeller(seller);
        String otp = OtpUtil.generateOtp();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCodeRepository.save(verificationCode);

        String subject = "Bazaar Email Verification Code";
        String text = "Welcome to the Bazaar, verify your account using this link ";
        String frontend_url = "http://localhost:3000/verify-seller";
        emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(),subject,text+frontend_url);

        return new ResponseEntity<>(savedSeller,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws Exception{

        Seller seller = sellerService.getSellerById(id);
        return new ResponseEntity<>(seller,HttpStatus.OK);

    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByJwt(
            @RequestHeader("Authorization") String jwt) throws Exception{


        Seller seller = sellerService.getSellerProfile(jwt);

        return new ResponseEntity<>(seller,HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<List<Seller>> getAllSellers(@RequestParam(required = false)
                                                          AccountStatus accountStatus){
        return ResponseEntity.ok(sellerService.getAllSellers(accountStatus));


    }

    @PatchMapping
    public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt,
                                               @RequestBody Seller seller) throws Exception {

        Seller profile = sellerService.getSellerProfile(jwt);
        Seller updatedSeller = sellerService.updateSeller(profile.getId(),seller);

        return ResponseEntity.ok(updatedSeller);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception {

        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();





    }

}
