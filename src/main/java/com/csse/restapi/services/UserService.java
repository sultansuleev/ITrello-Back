package com.csse.restapi.services;

import com.csse.restapi.entities.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {


    Boolean existsByEmail(String email);
    Users createUser(Users user);

    String getPasswordByEmail(String email);
    Users getByEmail(String email);

}