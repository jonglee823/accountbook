package com.accountbook.controller;

import com.accountbook.domain.Session;
import com.accountbook.domain.User;
import com.accountbook.repository.SessionRepository;
import com.accountbook.repository.UserRepository;
import com.accountbook.request.Login;
import com.accountbook.response.SessionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;

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
    UserRepository userRepository;

    @Autowired
    SessionRepository sessionRepository;

    @BeforeEach
    public void clean(){
        userRepository.deleteAll();
        sessionRepository.deleteAll();
    }

    @Test
    @DisplayName("reqeust ID/PW")
    void reqeustPostIdAndPw() throws Exception {
        //given
        Login login = Login.builder()
                .email("jh2@kakao.com")
                .password("1234")
                .build();

        String value = objectMapper.writeValueAsString(login);

        //EXPECTED
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(value)
                        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
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
    @DisplayName("회원 등록 후 로그인 성공")
    void createMemberAndLogin() throws Exception {
        //given
        userRepository.save(User.builder()
                            .name("이종혁")
                            .email("jh2@kakao.com")
                            .password("1234")
                            .build()
        );

        Login login = Login.builder()
                .email("jh2@kakao.com")
                .password("1234")
                .build();

        String value = objectMapper.writeValueAsString(login);

        //EXPECTED
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(value)
                )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    @Transactional
    @DisplayName("회원 등록 후 세션 생성")
    void createMemberAndSession() throws Exception {
        //given
        User user = userRepository.save(User.builder()
                .name("이종혁")
                .email("jh2@kakao.com")
                .password("1234")
                .build()
        );

        Login login = Login.builder()
                .email("jh2@kakao.com")
                .password("1234")
                .build();

        String value = objectMapper.writeValueAsString(login);

        //EXPECTED
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(value)
                )
                .andDo(print())
                .andExpect(status().isOk())

        ;

        assertEquals(1L, user.getSessions().size());
    }

    @Test
    @Transactional
    @DisplayName("회원 등록 후 세션 확인")
    void createMemberAndCheckSession() throws Exception {
        //given
        User user = userRepository.save(User.builder()
                .name("이종혁")
                .email("jh2@kakao.com")
                .password("1234")
                .build()
        );

        Login login = Login.builder()
                .email("jh2@kakao.com")
                .password("1234")
                .build();

        String value = objectMapper.writeValueAsString(login);

        //EXPECTED
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(value)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(user.getSessions().get(0).getToken()))

        ;

        assertEquals(1L, user.getSessions().size());
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

        //EXPECTED
        mockMvc.perform(get("/index")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", session.getToken())
                )
                .andDo(print())
                .andExpect(status().isOk())
        ;

        assertEquals(1L, user.getSessions().size());
    }

    @Test
    @Transactional
    @DisplayName("회원 등록 & 잘못된 token 요청시 401")
    void InvalidTokenAnd401() throws Exception {
        //given
        User user = userRepository.save(User.builder()
                .name("이종혁")
                .email("jh2@kakao.com")
                .password("1234")
                .build()
        );

        Session session = user.addSession();
        userRepository.save(user);

        //EXPECTED
        mockMvc.perform(get("/index")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", session.getToken()+"_____TOKEN")
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
        ;

        assertEquals(1L, user.getSessions().size());
    }
}