package com.csse.restapi.repositories;

import com.csse.restapi.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByUserEmail(String email);
}
