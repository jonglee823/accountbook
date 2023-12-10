package com.accountbook.request;

import lombok.Getter;

@Getter
public class LoginInfo {

    private String email;
    private String password;
    private String remember;

}
