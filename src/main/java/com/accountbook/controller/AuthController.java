package com.accountbook.controller;

import com.accountbook.api.PostController;
import com.accountbook.config.APPConfig;
import com.accountbook.domain.Siginup;
import com.accountbook.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    private final APPConfig appConfig;

    @PostMapping("/auth/signup")
    public void signup(@RequestBody Siginup signup){
        authService.signup(signup);
    }
}
