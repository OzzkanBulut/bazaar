package com.ozkan.bazaar.request;

import com.ozkan.bazaar.domain.USER_ROLE;
import lombok.Data;

@Data
public class LoginOtpRequest {

    private String email;
    private String otp;
    private USER_ROLE role;
}
