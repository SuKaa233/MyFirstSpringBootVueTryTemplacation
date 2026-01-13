package com.study.service.impl;

import com.study.entity.Account;
import com.study.mapper.UserMapper;
import com.study.service.AuthorizeService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AuthorizeServiceImpl implements AuthorizeService {

    @Value("${spring.mail.username}")
    String from;

    @Resource
    UserMapper mapper;

    @Resource
    MailSender mailSender;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    BCryptPasswordEncoder bCryptPasswordEncoder =  new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username == null)
            throw new UsernameNotFoundException("用户名不能为空");
        Account account = mapper.findAccountByNameOrEmail(username);
        if(account == null)
            throw new UsernameNotFoundException("用户名或者密码错误");
        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles("user")
                .build();
    }

    @Override
    public String sendValidationEmail(String email, String sessionId) {
        if(mapper.findAccountByNameOrEmail(email) != null){
            return "此邮箱已经被其他用户注册";
        }
        String key = "email:" + email + ":" + sessionId;
        if(Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))){
            Long expire = Optional.ofNullable(stringRedisTemplate.getExpire(key, TimeUnit.SECONDS)).orElse(0L);
            if(expire > 120){
                return "请求频繁,请稍后再试";
            }
        }
        Random random = new Random();
        String code = String.valueOf(random.nextInt(899999)+100000);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("验证码:");
        message.setText(code);
        try {
            mailSender.send(message);
            stringRedisTemplate.opsForValue().set(email,code,3,TimeUnit.MINUTES);
            stringRedisTemplate.opsForValue().set(key, code,3, TimeUnit.MINUTES);
            return null;
        }catch (MailException e){
            e.printStackTrace();
            return "邮件发送失败";
        }
    }

    @Override
    public String validateAndRegister(String username, String password, String email, String code,String sessionId) {
        if(Boolean.TRUE.equals(stringRedisTemplate.hasKey(email))){
            String s = stringRedisTemplate.opsForValue().get(email);
            if(s==null){
                return "验证码失效";
            }
            if (s.equals(code)){
                password=bCryptPasswordEncoder.encode(password);
                if(mapper.creatAccount(username,password,email) > 0){
                    return null;
                }else{
                    return "内部错误";
                }
            }else {
                return "验证码错误";
            }
        }else {
            return "请先获取验证码";
        }
    }
}
