package org.lab4_web.model;

import lombok.Setter;
import lombok.Getter;

import java.io.Serializable;


@Getter
@Setter
public class User implements Serializable{
    private String username;
    private String password;
    public User(){

    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

}
