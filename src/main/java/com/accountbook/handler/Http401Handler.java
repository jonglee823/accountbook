package com.accountbook.handler;

import com.accountbook.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class Http401Handler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    private static final String MESSAGE = "[인증오류] 로그인이 필요합니다.";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("401")
                .message(MESSAGE)
                .build();

        String json = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(SC_BAD_REQUEST);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(UTF_8.name());
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
