package com.csse.restapi.repositories;

import com.csse.restapi.entities.Card;
import com.csse.restapi.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);
    boolean existsByEmail(String email);
}
