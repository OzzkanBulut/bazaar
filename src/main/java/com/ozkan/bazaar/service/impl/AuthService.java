package com.ozkan.bazaar.service.impl;


import com.ozkan.bazaar.config.JwtProvider;
import com.ozkan.bazaar.domain.USER_ROLE;
import com.ozkan.bazaar.model.Cart;
import com.ozkan.bazaar.model.User;
import com.ozkan.bazaar.model.VerificationCode;
import com.ozkan.bazaar.repository.ICartRepository;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ICartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final IVerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final CustomUserDetailsService customUserDetailsService;


    @Override
    public void sentLoginOtp(String email) throws Exception {
        String SIGNING_PREFIX="signin_";

        if(email.startsWith(SIGNING_PREFIX)){
            email.substring(SIGNING_PREFIX.length());

            User user = userRepository.findByEmail(email);

            if(user==null){
                throw new Exception("User not exist with provided email");
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
        verificationCodeRepository.save(verificationCode);

        String subject = "The bazaar login/signup otp";
        String text = "Your otp is - "+otp;


        emailService.sendVerificationOtpEmail(email,otp,subject,text);

    }

    @Override
    public String createUser(SignUpRequest request) throws Exception {

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(request.getEmail());

        if(verificationCode==null || !verificationCode.getOtp().equals(request.getOtp())){
            throw new Exception("Wrong otp!");
        }

        User user = userRepository.findByEmail(request.getEmail());

        if(user==null){
            User createdUser = new User();
            createdUser.setEmail(request.getEmail());
            createdUser.setFullName(request.getFullName());
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createdUser.setMobile("1234567891");
            createdUser.setPassword(passwordEncoder.encode(request.getOtp()));

            userRepository.save(createdUser);

            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(request.getEmail(),null,authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);

    }

    @Override
    public AuthResponse signin(LoginRequest loginrequest) {
        String userName = loginrequest.getEmail();
        String otp = loginrequest.getOtp();

        Authentication authentication = authenticate(userName,otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login Success");

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String roleName = authorities.isEmpty()?null: authorities.iterator().next().getAuthority();

        authResponse.setRole(USER_ROLE.valueOf(roleName));

        return authResponse;

    }

    private Authentication authenticate(String userName, String otp) {

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);

        if(userDetails==null){
            throw new BadCredentialsException(("Invalid username"));
        }
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(userName);

        if(verificationCode==null || !verificationCode.getOtp().equals(otp)){
            throw new BadCredentialsException("Wrong Otp");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

    }
}
