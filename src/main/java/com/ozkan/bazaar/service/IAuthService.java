package com.ozkan.bazaar.service;

import com.ozkan.bazaar.domain.USER_ROLE;
import com.ozkan.bazaar.request.LoginRequest;
import com.ozkan.bazaar.response.AuthResponse;
import com.ozkan.bazaar.response.SignUpRequest;

public interface IAuthService {

    void sentLoginOtp(String email, USER_ROLE role) throws Exception;
    String createUser(SignUpRequest request) throws Exception;
    AuthResponse signin(LoginRequest loginrequest) throws Exception;



}
