package com.accountbook.config;

import org.springframework.security.test.context.support.WithSecurityContext;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = AccountMockSecurityContext.class )
public @interface AccountMockUser {

    String email() default  "jh2@kakao.com";

    String name() default "종혁";

    String password() default  "";

    String role() default "ROLE_ADMIN";
}
