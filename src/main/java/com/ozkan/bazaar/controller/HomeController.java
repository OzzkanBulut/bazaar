package com.ozkan.bazaar.controller;

import com.ozkan.bazaar.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class HomeController {

    @GetMapping()
    public ApiResponse HomeControllerHandler(){

        ApiResponse response = new ApiResponse();
        response.setMessage("Welcome to bazaar!");
        return response;

    }
}
