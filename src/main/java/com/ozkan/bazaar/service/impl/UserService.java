package com.ozkan.bazaar.service.impl;

import com.ozkan.bazaar.config.JwtProvider;
import com.ozkan.bazaar.model.Address;
import com.ozkan.bazaar.model.User;
import com.ozkan.bazaar.repository.IAddressRepository;
import com.ozkan.bazaar.repository.IUserRepository;
import com.ozkan.bazaar.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {


    private final IUserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final IAddressRepository addressRepository;


    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);

        return this.findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new Exception("User not found with email - " + email));
        return user;
    }

    @Override
    public Address findAddressById(Long addressId) {
        if (addressId == null) {
            throw new IllegalArgumentException("Address ID must not be null");
        }
        return addressRepository.findById(addressId).get();
    }
}
