package com.study.controller;

import com.study.entity.RestBean;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import com.study.service.AuthorizeService;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("api/auth")
public class AuthorizeController {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    @Resource
    AuthorizeService authorizeService;

    @PostMapping("/valid-email")
    public RestBean<String> validateEamil(@Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email,
                                          HttpSession session) {
        if(authorizeService.sendValidationEmail(email,session.getId()))
            return RestBean.success("邮件发送成功!");
        else
            return RestBean.failure(400,"邮件发送失败!请联系管理员");
    }
}
