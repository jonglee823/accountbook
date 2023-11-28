package com.accountbook.service;

import com.accountbook.domain.Session;
import com.accountbook.domain.User;
import com.accountbook.exception.InvalidLoginInfo;
import com.accountbook.repository.UserRepository;
import com.accountbook.request.Login;
import com.accountbook.response.SessionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserRepository userRepository;

        @Transactional
        public SessionResponse signin(Login request){
            User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                    .orElseThrow(()-> new InvalidLoginInfo());

        Session session = user.addSession();
        return SessionResponse.builder()
                .session(session)
                .build();
    }
}
