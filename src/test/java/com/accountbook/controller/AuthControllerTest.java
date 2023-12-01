package com.accountbook.controller;

import com.accountbook.config.APPConfig;
import com.accountbook.domain.Session;
import com.accountbook.domain.User;
import com.accountbook.repository.UserRepository;
import com.accountbook.request.Login;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = {"local"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private APPConfig appConfig;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void clean(){
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("reqeust Post Invalid ID/PW")
    void PostInvalidIdAndPw() throws Exception {
        //given
        Login login = Login.builder()
                .email("jh2@kakao.com")
                .password("1111")
                .build();

        String value = objectMapper.writeValueAsString(login);

        //EXPECTED
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(value)
                )
                .andDo(print())
                .andExpect(status().is4xxClientError())
        ;
    }

    @Test
    @Transactional
    @DisplayName("회원 등록 후 DB에서 세션 확인")
    void createMemberAndCheckSessionInDB() throws Exception {
        //given
        User user = userRepository.save(User.builder()
                .name("이종혁")
                .email("jh2@kakao.com")
                .password("1234")
                .build()
        );

        Session session = user.addSession();
        userRepository.save(user);

        String jws = Jwts.builder()
                .subject(user.getId().toString())
                .signWith(appConfig.getSecretKey())
                .issuedAt(new Date())
                .compact();

        //EXPECTED
        mockMvc.perform(get("/index")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", jws)
                )
                .andDo(print())
                .andExpect(status().isOk())
        ;

        assert Jwts.parser().verifyWith(appConfig.getSecretKey())
                .build()
                .parseSignedClaims(jws)
                .getPayload()
                .getSubject()
                .equals(user.getId().toString());
    }

}