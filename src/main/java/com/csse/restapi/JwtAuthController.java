package com.csse.restapi;

import com.csse.restapi.dto.JwtRequest;
import com.csse.restapi.dto.JwtResponse;
import com.csse.restapi.dto.MessageResponse;
import com.csse.restapi.entities.Roles;
import com.csse.restapi.entities.Users;
import com.csse.restapi.jwt.JwtTokenGenerator;
import com.csse.restapi.services.RoleService;
import com.csse.restapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@CrossOrigin
public class JwtAuthController {

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthenticationManager authenticationManager;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @RequestMapping(value = "/auth")
    public ResponseEntity<?> auth(@RequestBody JwtRequest request) throws Exception{
        authenticate(request.getEmail(), passwordEncoder.matches(request.getPassword(), userService.getPasswordByEmail(request.getEmail()))?request.getPassword():"re");
        final UserDetails userDetails =
                userService.loadUserByUsername(request.getEmail());

        final String token = jwtTokenGenerator.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));

    }

    @GetMapping(value = "/getUser/{token}")
    public ResponseEntity<?> getUser(@PathVariable String token) {
        String email = jwtTokenGenerator.getEmailFromToken(token);
        Users user = (Users) userService.loadUserByUsername(email);
        return ResponseEntity.ok(user);
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<?> register(@RequestBody JwtRequest request){
        if(userService.existsByEmail(request.getEmail())){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        Users user = new Users();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Set<Roles> roles = new HashSet<>();
        roles.add(roleService.getRoleByName("ROLE_USER"));
        userService.createUser(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    public void authenticate(String email, String password) throws Exception {
        try{

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        }catch (DisabledException e){
            throw new Exception("USER_DISABLED", e);
        }catch (BadCredentialsException e){
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}