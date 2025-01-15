package com.ozkan.bazaar.service;

import com.ozkan.bazaar.model.User;

public interface IUserService {

    public User findUserByJwtToken(String jwt) throws Exception;
    public User findUserByEmail(String email) throws Exception;
}
