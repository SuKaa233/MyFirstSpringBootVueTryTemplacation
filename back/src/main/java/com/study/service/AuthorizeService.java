package com.study.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthorizeService extends UserDetailsService {
    String sendValidationEmail(String email , String sessionId);
    String validateAndRegister(String username , String password,String email,String code,String sessionId);
}
