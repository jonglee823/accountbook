package com.accountbook.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

public class UserPrincipal extends User {

    private final Long id;

    public UserPrincipal(com.accountbook.domain.User user) {
        super(user.getEmail(), user.getPassword(),
//                List.of(new SimpleGrantedAuthority("ROLE_USER")
//                        ,new SimpleGrantedAuthority("WRITE")
//                )
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")
                )
            );
        this.id = user.getId();
    }

    public Long getId() {
        return id;
    }
}
