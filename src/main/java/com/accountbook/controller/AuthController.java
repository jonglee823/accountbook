package com.accountbook.controller;

import com.accountbook.domain.User;
import com.accountbook.exception.InvalidLoginInfo;
import com.accountbook.repository.UserRepository;
import com.accountbook.request.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping("/auth/login")
    public User login(@RequestBody Login login){
        //json ID/ PW
        log.info(">>>>>>={}", login);

        //DB 조회
        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(()-> new InvalidLoginInfo());

        //TOKEN 응답
    return user;
    }

}
