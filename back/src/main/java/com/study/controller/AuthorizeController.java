package com.study.controller;

import com.study.entity.RestBean;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import com.study.service.AuthorizeService;
import jakarta.annotation.Resource;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("api/auth")
public class AuthorizeController {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String USERNAME_REGEX = "^[A-zA-Z0-9\\u4e00-\\u9fa5]+$";

    @Resource
    AuthorizeService authorizeService;

    @PostMapping("/valid-email")
    public RestBean<String> validateEamil(@Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email,
                                          HttpSession session) {
        String s = authorizeService.sendValidationEmail(email,session.getId());
        if(s==null)
            return RestBean.success("邮件发送成功!");
        else
            return RestBean.failure(400,s);
    }

    @PostMapping("/register")
    public RestBean<String> registeUser(@Pattern(regexp = USERNAME_REGEX) @Length(min = 2,max = 25) @RequestParam("username") String username
            ,@Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email
            ,@Length(min = 6,max = 15) @RequestParam("password") String password
            ,@Length(min = 6,max = 6)@RequestParam("code") String code
            ,String sessionId){
        String s = authorizeService.validateAndRegister(username,password,email,code,sessionId);
        if (s==null){
            return RestBean.success("注册成功");
        }else {
            return RestBean.failure(400,s);
        }

    }
}
