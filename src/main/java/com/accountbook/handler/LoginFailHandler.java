package com.accountbook.handler;

import com.accountbook.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static jakarta.servlet.http.HttpServletResponse.*;
import static java.nio.charset.StandardCharsets.*;
import static org.springframework.http.MediaType.*;

@Slf4j
@RequiredArgsConstructor
public class LoginFailHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    private final String MESSAGE ="[인증오류] 아이디 혹은 비밀번호가 올바르지 않습니다.";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        ErrorResponse errorResponse = ErrorResponse.builder()
                                                    .code("400")
                                                    .message(MESSAGE)
                                                    .build();

        String json = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(SC_BAD_REQUEST);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
