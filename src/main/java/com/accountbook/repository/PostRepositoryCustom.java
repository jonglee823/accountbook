package com.accountbook.repository;

import com.accountbook.domain.Post;
import com.accountbook.request.PostSearch;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
