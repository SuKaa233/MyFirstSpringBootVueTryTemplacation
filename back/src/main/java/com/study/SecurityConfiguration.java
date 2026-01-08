package com.study;

import com.alibaba.fastjson2.JSONObject;
import com.study.entity.RestBean;
import com.study.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Resource
    AuthorizeService authorizeService;

    @Bean
    SecurityFilterChain chain(HttpSecurity http) {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(a -> a.anyRequest().authenticated())
                .formLogin(f -> f
                        .failureHandler(this::AuthenticationFailureHandler)
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler(this::onAuthenticationSuccess)
                )
                .logout(l -> l
                        .logoutUrl("/api/auth/logout")
                )
                .exceptionHandling(e->e
                        .authenticationEntryPoint(this::AuthenticationFailureHandler)
                )
                .userDetailsService(authorizeService)
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity security){
//        return security
//                .getSharedObject(AuthenticationManagerBuilder.class)
//                .userDetailsService(authorizeService)
//                .and()
//                .build();
//    }

    private void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        httpServletRequest.setCharacterEncoding("utf-8");
        httpServletResponse.getWriter().write(JSONObject.toJSONString(RestBean.success("登录成功！")));
    }

    public void AuthenticationFailureHandler(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(JSONObject.toJSONString(RestBean.failure(401,exception.getMessage())));
    }

}
