package com.study.entity.auth;

import lombok.Data;

@Data
public class Account {
    String email;
    String username;
    int id;
    String password;
}
