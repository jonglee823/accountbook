package com.accountbook.config;

import com.accountbook.repository.PostRepository;
import com.querydsl.core.annotations.Config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity()
@RequiredArgsConstructor
@Slf4j
public class MethodSecurityConfig {

    public final PostRepository postRepository;

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(){
        var handler = new DefaultMethodSecurityExpressionHandler();
        handler.setPermissionEvaluator(new AccountbookPermissionEvaluator(postRepository));
        return handler;
    }
}
