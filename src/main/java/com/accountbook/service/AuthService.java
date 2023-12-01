package com.accountbook.service;

import com.accountbook.domain.Siginup;
import com.accountbook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;


    @Transactional
    public void signup(Siginup signup){

    }
}
