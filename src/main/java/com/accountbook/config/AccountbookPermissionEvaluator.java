package com.accountbook.config;

import com.accountbook.domain.Post;
import com.accountbook.exception.PostNotFound;
import com.accountbook.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

@Slf4j
@RequiredArgsConstructor
public class AccountbookPermissionEvaluator implements PermissionEvaluator {

    public final PostRepository postRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Post post = postRepository.findById((Long) targetId)
                                    .orElseThrow(PostNotFound::new);

        if (!post.getUserId().equals(userPrincipal.getId())){
            log.error("[인가오류] 권한이 없습니다. >>>>>> {} ", targetId);

            return false;
        }


        return true;
    }
}
