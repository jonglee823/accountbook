package com.accountbook.config;

import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.crypto.SecretKey;


@Slf4j
@Data
@ConfigurationProperties(prefix = "account")
public class APPConfig {

    public String domain;
    private  String key;


    public SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(this.key.getBytes());
    }
}
