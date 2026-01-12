package com.study.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthorizeService extends UserDetailsService {
    boolean sendValidationEmail(String email , String sessionId);
}
