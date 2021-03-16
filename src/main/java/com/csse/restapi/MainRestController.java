package com.csse.restapi;

import com.csse.restapi.dto.CardRequest;
import com.csse.restapi.dto.JwtRequest;
import com.csse.restapi.dto.MessageResponse;
import com.csse.restapi.entities.Card;
import com.csse.restapi.entities.CardTask;
import com.csse.restapi.entities.Roles;
import com.csse.restapi.entities.Users;
import com.csse.restapi.jwt.JwtTokenGenerator;
import com.csse.restapi.repositories.CardRepository;
import com.csse.restapi.repositories.CardTaskRepository;
import com.csse.restapi.services.CardService;
import com.csse.restapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(value = "/api")
public class MainRestController {

    @Autowired
    private CardService cardService;

    @Autowired
    private CardTaskRepository cardTaskRepository;

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    @Autowired
    private UserService userService;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/cards/{jwtToken}")
    public List<Card> getAllItems(@PathVariable String jwtToken){
        String email = jwtTokenGenerator.getEmailFromToken(jwtToken);
        List<Card> cards = cardService.findAllByUserEmail(email);
        return cards;
    }

    @PostMapping("/addCard")
    public Card addItem(@RequestBody CardRequest cardRequest){
        System.out.println(cardRequest);

        Date now = new Date(System.currentTimeMillis());
        Card card = new Card();
        card.setName(cardRequest.getName());
        card.setAddedDate(now);

        String email = jwtTokenGenerator.getEmailFromToken(cardRequest.getJwtToken());
        Users user = (Users) userService.loadUserByUsername(email);
        System.out.println(user.getFullName() + "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
        card.setUser(user);
        return cardService.save(card);
    }

    @PostMapping("/editCard")
    public Card editItem(@RequestBody CardRequest cardRequest){
        System.out.println(cardRequest);
        Card card = cardService.getOne(cardRequest.getId());
        Date now = new Date(System.currentTimeMillis());
        card.setName(cardRequest.getName());
        card.setAddedDate(now);
        return cardService.save(card);
    }

    @PostMapping("/addCardTask/{id}")
    public CardTask addItem(@PathVariable Long id, @RequestBody CardTask cardTask){
        cardTask.setCard(cardService.findById(id));
        return cardTaskRepository.save(cardTask);
    }

    @GetMapping("/card/{id}")
    public Card getCardById(@PathVariable Long id){
        return cardService.findById(id);
    }

    @GetMapping("/cardTasks/{id}")
    public List<CardTask> getCardTasksById(@PathVariable Long id){
        return cardTaskRepository.findAllByCard(cardService.findById(id));
    }

    @PutMapping("/cardTask/{id}")
    public CardTask updateCardTask(@PathVariable Long id){
        CardTask old = cardTaskRepository.findById(id).get();
        old.setDone(!old.isDone());
        return cardTaskRepository.save(old);
    }

    @DeleteMapping("/card/{id}")
    void deleteCard(@PathVariable Long id) {
        cardTaskRepository.deleteAllByCard(cardService.findById(id));
        cardService.deleteById(id);
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
