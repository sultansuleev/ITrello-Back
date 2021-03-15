package com.csse.restapi.services;

import com.csse.restapi.entities.Roles;
import com.csse.restapi.entities.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface RoleService {

    Roles getRoleByName(String role);

}