package com.accountbook;

import com.accountbook.config.APPConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties(APPConfig.class)
public class AccountbookApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountbookApplication.class, args);
    }

}
