package com.study.config;

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
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.io.IOException;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Resource
    AuthorizeService authorizeService;

    @Resource
    DataSource dataSource;

    public

    @Bean
    SecurityFilterChain chain(HttpSecurity http) {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(f -> f
                        .failureHandler(this::AuthenticationFailureHandler)
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler(this::onAuthenticationSuccess)
                )
                .logout(l -> l
                        .logoutSuccessHandler(this::onAuthenticationSuccess)
                        .logoutUrl("/api/auth/logout")
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(this::AuthenticationFailureHandler)
                )
                .rememberMe(rm -> rm.rememberMeParameter("remember").tokenRepository(tokenRepository(dataSource)).tokenValiditySeconds(3600*24*7))
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .userDetailsService(authorizeService)
                .build();
    }

    @Bean
    public PersistentTokenRepository tokenRepository(DataSource dataSource) {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        //noinspection removal
        tokenRepository.setDataSource(dataSource);
        tokenRepository.setCreateTableOnStartup(false);//第一次运行记得打开
        return tokenRepository;
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowCredentials(true);
        cfg.addAllowedOrigin("http://localhost:5173"); // 前端端口
        cfg.addAllowedHeader("*");
        cfg.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return src;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
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
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        if (httpServletRequest.getRequestURI().endsWith("/login")) {
            httpServletResponse.getWriter().write(JSONObject.toJSONString(RestBean.success("登录成功！")));
        } else if (httpServletRequest.getRequestURI().endsWith("/logout")) {
            httpServletResponse.getWriter().write(JSONObject.toJSONString(RestBean.success("退出登录成功！")));
        }
    }

    public void AuthenticationFailureHandler(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(JSONObject.toJSONString(RestBean.failure(401, exception.getMessage())));
    }

}
