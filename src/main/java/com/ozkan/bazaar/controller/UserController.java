package com.ozkan.bazaar.controller;

import com.ozkan.bazaar.model.User;
import com.ozkan.bazaar.response.AuthResponse;
import com.ozkan.bazaar.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("users/profile")
    public ResponseEntity<User> createUserHandler(@RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        return ResponseEntity.ok(user);
    }
}
