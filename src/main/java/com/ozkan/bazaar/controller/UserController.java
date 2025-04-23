package com.ozkan.bazaar.controller;

import com.ozkan.bazaar.model.Address;
import com.ozkan.bazaar.model.User;
import com.ozkan.bazaar.repository.IAddressRepository;
import com.ozkan.bazaar.repository.IUserRepository;
import com.ozkan.bazaar.response.AuthResponse;
import com.ozkan.bazaar.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final IUserRepository userRepository;
    private final IAddressRepository addressRepository;

    @GetMapping("users/profile")
    public ResponseEntity<User> createUserHandler(@RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        System.out.println(user.toString());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<Address>> getUserAddresses(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        return ResponseEntity.ok(new ArrayList<>(user.getAddresses()));
    }

    @PostMapping("/addresses")
    public ResponseEntity<Address> addUserAddress(@RequestHeader("Authorization") String jwt, @RequestBody Address address) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        // Save the address before adding it to the user
        addressRepository.save(address);  // Ensure address is persisted

        user.getAddresses().add(address);  // Add address to user

        // Optionally, you can save the user again to persist the relationship
        userRepository.save(user);

        return ResponseEntity.ok(address);
    }





}
