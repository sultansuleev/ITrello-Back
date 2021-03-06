package com.csse.restapi.repositories;

import com.csse.restapi.entities.Card;
import com.csse.restapi.entities.CardTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface CardTaskRepository extends JpaRepository<CardTask, Long> {
    List<CardTask> findAllByCard(Card card);
    void deleteAllByCard(Card card);
}
