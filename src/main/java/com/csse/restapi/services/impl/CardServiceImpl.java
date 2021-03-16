package com.csse.restapi.services.impl;

import com.csse.restapi.entities.Card;
import com.csse.restapi.entities.Users;
import com.csse.restapi.repositories.CardRepository;
import com.csse.restapi.repositories.UsersRepository;
import com.csse.restapi.services.CardService;
import com.csse.restapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Override
    public List<Card> findAllByUserEmail(String email) {
        return cardRepository.findAllByUserEmail(email);
    }

    @Override
    public Card getOne(Long id) {
        return cardRepository.getOne(id);
    }

    @Override
    public Card save(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public Card findById(Long id) {
        return cardRepository.findById(id).get();
    }

    @Override
    public void deleteById(Long id) {
        cardRepository.deleteById(id);
    }
}