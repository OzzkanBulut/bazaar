package com.ozkan.bazaar.controller;

import com.ozkan.bazaar.model.Address;
import com.ozkan.bazaar.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final IUserService userService;

    // Endpoint to add a new address for the logged-in user
    @PostMapping
    public Address addAddress(@RequestBody Address address, @RequestHeader("Authorization") String authorizationHeader) throws Exception {
        // Extract JWT from Authorization header
        String jwt = authorizationHeader.substring(7); // "Bearer " is 7 characters long
        return userService.addAddressToUser(jwt, address);
    }

    // Additional endpoints as needed
}
