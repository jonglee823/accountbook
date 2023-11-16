package com.accountbook.repository;

import com.accountbook.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.client.RestTemplate;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {


}


