package com.accountbook.repository;

import com.accountbook.domain.Session;
import com.accountbook.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {

    Optional<Session> findByToken(String token);


}
