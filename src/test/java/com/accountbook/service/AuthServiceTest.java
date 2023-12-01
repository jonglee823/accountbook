package com.accountbook.service;

import com.accountbook.repository.UserRepository;
import com.accountbook.request.Login;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = {"local"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void clean(){
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 등록 후 세션 생성")
    void createMemberAndSession() throws Exception {
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



}