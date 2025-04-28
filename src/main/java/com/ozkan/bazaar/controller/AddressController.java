package com.ozkan.bazaar.controller;

import com.ozkan.bazaar.model.Address;
import com.ozkan.bazaar.model.User;
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
    public Address addAddress(@RequestBody Address address, @RequestHeader("Authorization") String jwt) throws Exception {
        // Extract JWT from Authorization header
        User user = userService.findUserByJwtToken(jwt);
        return userService.addAddressToUser(user, address);
    }

    // Additional endpoints as needed
}
