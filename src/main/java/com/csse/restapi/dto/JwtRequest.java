package com.csse.restapi.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 123456789L;
    private String email;
    private String password;
    private String fullName;

    JwtRequest(String email, String password){
        this.email = email;
        this.password = password;
    }
}