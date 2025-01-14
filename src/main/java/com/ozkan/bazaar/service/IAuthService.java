package com.ozkan.bazaar.service;

import com.ozkan.bazaar.response.SignUpRequest;

public interface IAuthService {

    void sentLoginOtp(String email) throws Exception;
    String createUser(SignUpRequest request) throws Exception;


}
