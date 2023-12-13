package com.accountbook.config;

import com.accountbook.domain.User;
import com.accountbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;


@RequiredArgsConstructor
public class AccountMockSecurityContext implements WithSecurityContextFactory<AccountMockUser> {

    private final UserRepository userRepository;

    @Override
    public SecurityContext createSecurityContext(AccountMockUser annotation) {
        var user = User.builder()
                .email(annotation.email())
                .password(annotation.password())
                .build();

        userRepository.save(user);

        var principal = new UserPrincipal(user);

        var role = new SimpleGrantedAuthority("ROLE_ADMIN");
        var authentication = new UsernamePasswordAuthenticationToken(principal
                        , user.getPassword()
                        , List.of(role));

        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

    return context;
    }
}
