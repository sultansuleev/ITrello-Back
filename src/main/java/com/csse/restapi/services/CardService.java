package com.csse.restapi.services;

import com.csse.restapi.entities.Card;
import com.csse.restapi.entities.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface CardService {
    List<Card> findAllByUserEmail(String email);
    Card getOne(Long id);
    Card save(Card card);
    Card findById(Long id);
    void deleteById(Long id);
}