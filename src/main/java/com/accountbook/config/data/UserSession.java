package com.accountbook.config.data;

import com.accountbook.domain.Session;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSession {

    public Long id;

    public String token;

    public UserSession(Long id, String token) {
        this.id = id;
        this.token = token;
    }

    public UserSession(Session session) {
        this.id = session.getId();
        this.token = session.getToken();
    }
}