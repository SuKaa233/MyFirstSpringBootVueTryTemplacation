package com.study.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthorizeService extends UserDetailsService {
    String sendValidationEmail(String email , String sessionId,boolean hasAccount);
    String validateAndRegister(String username , String password,String email,String code,String sessionId);
    String validateOnly(String email,String code,String sessionId);
    boolean resetPassword(String password,String email);
}
