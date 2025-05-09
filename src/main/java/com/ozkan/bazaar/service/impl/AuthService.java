package com.ozkan.bazaar.service.impl;


import com.ozkan.bazaar.config.JwtProvider;
import com.ozkan.bazaar.domain.USER_ROLE;
import com.ozkan.bazaar.model.Cart;
import com.ozkan.bazaar.model.Seller;
import com.ozkan.bazaar.model.User;
import com.ozkan.bazaar.model.VerificationCode;
import com.ozkan.bazaar.repository.ICartRepository;
import com.ozkan.bazaar.repository.ISellerRepository;
import com.ozkan.bazaar.repository.IUserRepository;
import com.ozkan.bazaar.repository.IVerificationCodeRepository;
import com.ozkan.bazaar.request.LoginRequest;
import com.ozkan.bazaar.response.AuthResponse;
import com.ozkan.bazaar.response.SignUpRequest;
import com.ozkan.bazaar.service.IAuthService;
import com.ozkan.bazaar.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final IUserRepository userRepository;
    private final ISellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;
    private final ICartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final IVerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final CustomUserDetailsService customUserDetailsService;


    @Override
    public void sentLoginOtp(String email,USER_ROLE role) throws Exception {
        String SIGNING_PREFIX="signin_";
//        String SELLER_PREFIX="seller_";

        if(email.startsWith(SIGNING_PREFIX)){
            email = email.substring(SIGNING_PREFIX.length());

            if(role.equals(USER_ROLE.ROLE_SELLER)){

                Seller seller = sellerRepository.findByEmail(email);
                if(seller == null){
                    throw new Exception("Seller not found");
                }
            }
            else {

                User user = userRepository.findByEmail(email).orElseThrow(()->new Exception("User not exist with provided email"));

            }


        }

        VerificationCode doesExist = verificationCodeRepository.findByEmail(email);

        if (doesExist != null) {
            verificationCodeRepository.delete(doesExist);
        }

        String otp = OtpUtil.generateOtp();

        VerificationCode verificationCode = new VerificationCode();

        verificationCode.setOtp(otp);;
        verificationCode.setEmail(email);
        verificationCode.setCreatedAt(LocalDateTime.now()); // ✅ Add this line
        verificationCodeRepository.save(verificationCode);

        String subject = "Your Bazaar Login Code";

        String htmlContent =
                "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #eee; border-radius: 10px;\">\n" +
                        "    <div style=\"text-align: center;\">\n" +
                        "        <img src=\"https://drive.google.com/uc?export=view&id=1HAquy2tQAtmxpUVLlr53eUoSjegp82BW\"\n" +
                        "             alt=\"Bazaar Logo\"\n" +
                        "             style=\"width: 100%; max-height: 150px; object-fit: contain; border-radius: 10px; margin-bottom: 20px;\">\n" +
                        "        <h2>Welcome to Bazaar!</h2>\n" +
                        "    </div>\n" +
                        "    <p>Hello,</p>\n" +
                        "    <p>We received a request to sign in to your Bazaar account. Please use the following one-time code to complete your login:</p>\n" +
                        "    <div style=\"text-align: center; margin: 30px 0;\">\n" +
                        "        <div style=\"display: inline-block; padding: 10px 20px; font-size: 24px; color: #fff; background-color: #4CAF50; border-radius: 5px;\">\n" +
                        "            " + otp + "\n" +
                        "        </div>\n" +
                        "    </div>\n" +
                        "    <p>This code will expire shortly for your security. If you did not request this, you can safely ignore this email.</p>\n" +
                        "    <p>Thank you,<br>The Bazaar Team</p>\n" +
                        "    <hr>\n" +
                        "    <p style=\"font-size: 12px; color: #888; text-align: center;\">© 2025 Bazaar. All rights reserved.</p>\n" +
                        "</div>";







        emailService.sendVerificationOtpEmail(email,otp,subject,htmlContent);

    }

    @Override
    public String createUser(SignUpRequest request) throws Exception {
            

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(request.getEmail());

        if (verificationCode == null || !verificationCode.getOtp().equals(request.getOtp())) {
            throw new Exception("Wrong OTP!");
        }

        if (verificationCode.getCreatedAt().plusMinutes(3).isBefore(LocalDateTime.now())) {
            throw new Exception("OTP expired. Please request a new one.");
        }


        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());


        if (userOptional.isEmpty()) {
            // If user not found, create a new one
            User createdUser = new User();
            createdUser.setEmail(request.getEmail());
            createdUser.setFullName(request.getFullName());
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createdUser.setMobile("1234567891");
            createdUser.setPassword(passwordEncoder.encode(request.getOtp()));

            userRepository.save(createdUser);

            // Create a new Cart for the new user
            Cart cart = new Cart();
            cart.setUser(createdUser);  // Use the createdUser object
            cartRepository.save(cart);

            // Use createdUser since it wasn't found earlier
            userOptional = Optional.of(createdUser);
        }

        // Use the existing or newly created user
        User user = userOptional.get();

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(request.getEmail(),null,authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);

    }

    @Override
    public AuthResponse signin(LoginRequest loginrequest) throws Exception {
        String userName = loginrequest.getEmail();
        String otp = loginrequest.getOtp();

        Authentication authentication = authenticate(userName,otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login Success");

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

//        String roleName = authorities.isEmpty()?null: authorities.iterator().next().getAuthority();

        if (userName.startsWith("seller_")) {
            authResponse.setRole(USER_ROLE.ROLE_SELLER); // Set role explicitly as seller
        } else {
            authResponse.setRole(USER_ROLE.ROLE_CUSTOMER);
        }

//        authResponse.setRole(USER_ROLE.valueOf(roleName));

        return authResponse;

    }

    private Authentication authenticate(String userName, String otp) throws Exception {

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);

        String SELLER_PREFIX ="seller_";

        if(userName.startsWith(SELLER_PREFIX)){
            userName = userName.substring(SELLER_PREFIX.length());
        }

        if(userDetails==null){
            throw new Exception(("Invalid username"));
        }
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(userName);

        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new BadCredentialsException("Wrong OTP");
        }

        if (verificationCode.getCreatedAt().plusMinutes(3).isBefore(LocalDateTime.now())) {
            throw new BadCredentialsException("OTP expired. Please request a new one.");
        }



        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

    }


}
