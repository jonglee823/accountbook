package com.accountbook.config.filter;

import com.accountbook.request.LoginInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

@Slf4j
public class EmailPasswordAuthFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    public EmailPasswordAuthFilter(String loginUrl, ObjectMapper objectMapper) {
        super(loginUrl);
        this.objectMapper = objectMapper;
    }

    @Override
        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
            LoginInfo loginInfo = objectMapper.readValue(request.getInputStream(), LoginInfo.class);

            request.setAttribute("loginInfo", loginInfo);

            UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginInfo.getEmail(),
                    loginInfo.getPassword());

            authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));

            return this.getAuthenticationManager().authenticate(authRequest);
    }

}
