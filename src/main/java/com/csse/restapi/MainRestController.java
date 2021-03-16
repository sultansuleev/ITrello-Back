package com.csse.restapi;

import com.csse.restapi.dto.JwtRequest;
import com.csse.restapi.dto.MessageResponse;
import com.csse.restapi.entities.Card;
import com.csse.restapi.entities.CardTask;
import com.csse.restapi.entities.Roles;
import com.csse.restapi.entities.Users;
import com.csse.restapi.repositories.CardRepository;
import com.csse.restapi.repositories.CardTaskRepository;
import com.csse.restapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/api")
public class MainRestController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardTaskRepository cardTaskRepository;

    @Autowired
    private UserService userService;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/cards")
    public List<Card> getAllItems(){
        return cardRepository.findAll();
    }

    @PostMapping("/addCard")
    public Card addItem(@RequestBody Card card){
        return cardRepository.save(card);
    }

    @PostMapping("/addCardTask/{id}")
    public CardTask addItem(@PathVariable Long id, @RequestBody CardTask cardTask){
        cardTask.setCard(cardRepository.findById(id).get());
        return cardTaskRepository.save(cardTask);
    }

    @GetMapping("/card/{id}")
    public Card getCardById(@PathVariable Long id){
        return cardRepository.findById(id).get();
    }

    @GetMapping("/cardTasks/{id}")
    public List<CardTask> getCardTasksById(@PathVariable Long id){
        return cardTaskRepository.findAllByCard(cardRepository.findById(id).get());
    }

    @PutMapping("/cardTask/{id}")
    public CardTask updateCardTask(@PathVariable Long id){
        CardTask old = cardTaskRepository.findById(id).get();
        old.setDone(!old.isDone());
        return cardTaskRepository.save(old);
    }

    @DeleteMapping("/card/{id}")
    void deleteCard(@PathVariable Long id) {
        cardTaskRepository.deleteAllByCard(cardRepository.findById(id).get());
        cardRepository.deleteById(id);
    }

    @PutMapping("/changeName")
    public ResponseEntity<?> changeName(@RequestBody Users user) {
        Users existedUser = userService.getByEmail(user.getEmail());
        System.out.println(user);
        existedUser.setFullName(user.getFullName());
        userService.createUser(existedUser);

        return ResponseEntity.ok(existedUser);
    }

    @PutMapping("/changePass")
    public ResponseEntity<?> changePassword(@RequestBody JwtRequest request) {
        Users existedUser = userService.getByEmail(request.getEmail());

        if (passwordEncoder.matches(request.getPassword(), existedUser.getPassword())) {
            existedUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userService.createUser(existedUser);
            return ResponseEntity.ok(existedUser);
        }

        return ResponseEntity.badRequest().body(new MessageResponse("Old password does not match!"));
    }


}
