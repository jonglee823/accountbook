package com.accountbook.controller;

import com.accountbook.domain.Session;
import com.accountbook.domain.User;
import com.accountbook.exception.InvalidLoginInfo;
import com.accountbook.repository.UserRepository;
import com.accountbook.request.Login;
import com.accountbook.response.SessionResponse;
import com.accountbook.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    /*
    설명 : id, password json 요청시 로그인 및 토큰 생성 및 반환
    param: String Id, String Passwrod
    return : SessionResponse(token)
    */
//    @PostMapping("/auth/login")
//    public SessionResponse login(@RequestBody Login login){
//        log.info(">>>>>>={}", login);
//
//        return authService.signin(login);
//    }

    /*
    설명 : id, password json 요청시 로그인 및 토큰 생성 후 cookie 설정
    param: String Id, String Passwrod
    return : SessionResponse(token)
    */
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
        SessionResponse sessionResponse = authService.signin(login);

        ResponseCookie cookie = ResponseCookie.from("SESSION", sessionResponse.getToken())
                .domain("localhost")
                .httpOnly(true)
                .path("/")
                .sameSite("strict")
                .maxAge(Duration.ofDays(30))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,cookie.toString())
                .build();
    }

}
