package com.accountbook.repository;

import com.accountbook.domain.Post;
import com.accountbook.domain.QPost;
import com.accountbook.request.PostSearch;
import com.querydsl.core.types.Order;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Post> getList(PostSearch postsearch) {
        return jpaQueryFactory.selectFrom(QPost.post)
                .limit(postsearch.getSize())
                .offset(postsearch.getOffset())
                .orderBy(QPost.post.id.desc())
                .fetch();

    }
}