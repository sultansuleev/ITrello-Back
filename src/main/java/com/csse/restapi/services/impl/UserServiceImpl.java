package com.csse.restapi.services.impl;

import com.csse.restapi.entities.Users;
import com.csse.restapi.repositories.UsersRepository;
import com.csse.restapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(s);
        if(user!=null){
            return user;
        }else{
            throw new UsernameNotFoundException("USER NOT FOUND");
        }
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Users createUser(Users user) {
        return userRepository.save(user);
    }

    @Override
    public String getPasswordByEmail(String email) {
        return userRepository.findByEmail(email).getPassword();
    }

    @Override
    public Users getByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}