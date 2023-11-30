package com.accountbook.response;

import com.accountbook.domain.Session;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class SessionResponse {

    private final String token;

    @Builder
    public SessionResponse(String token) {
        this.token = token;

    }
}

