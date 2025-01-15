package com.ozkan.bazaar.service.impl;

import com.ozkan.bazaar.config.JwtProvider;
import com.ozkan.bazaar.model.User;
import com.ozkan.bazaar.repository.IUserRepository;
import com.ozkan.bazaar.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {


    private final IUserRepository userRepository;
    private final JwtProvider jwtProvider;


    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);

        return this.findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new Exception("User not found with email - " + email);
        }
        return user;
    }
}
