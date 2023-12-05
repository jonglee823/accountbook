package com.accountbook.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Siginup {

    public Siginup() {
    }

    public Siginup(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    private String email;

    private String password;

    private  String name;
}
