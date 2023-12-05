package com.accountbook.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Generated()
    private Long id;

    private String token;

    private LocalDateTime expirationTime;

    @ManyToOne
    private User user;

    @Builder
    public Session(User user) {
        this.token = UUID.randomUUID().toString();
        this.expirationTime = LocalDateTime.now();
        this.user = user;
    }
}
