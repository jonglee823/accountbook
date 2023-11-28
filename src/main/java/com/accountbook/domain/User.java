package com.accountbook.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    @Builder
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.createAt = LocalDateTime.now();
    }

    private LocalDateTime createAt;

    @OneToMany(cascade = ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<Session> sessions = new ArrayList<>();

    public Session addSession(){
        Session session = Session.builder()
                        .user(this)
                        .build();

        sessions.add(session);

        return session;
    }

}