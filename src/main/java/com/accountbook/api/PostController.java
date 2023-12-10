package com.accountbook.api;


import com.accountbook.request.PostEdit;
import com.accountbook.request.PostRequest;
import com.accountbook.request.PostSearch;
import com.accountbook.response.PostResponse;
import com.accountbook.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /*
        설명 : 게시글 작성
        param :
        return :
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostRequest request){
        postService.write(request);
    }

    /*
    설명 : 게시글 작성
    param :
    return :
    */
    //posts -> 글 전체 조회(검색 + 페이징)
    //posts/{postId} -> 글 한개만 조회
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name ="postId") Long id){
        return postService.get(id);
    }

    /*
    설명 : 게시글 작성
    param :
    return :
    */
    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/posts/{postId}")
    public PostResponse edit(@PathVariable(name="postId") Long postId, @RequestBody @Valid PostEdit postEdit){
        return postService.edit(postId, postEdit);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable(name="postId") Long postId){
        postService.delete(postId);
    }
}