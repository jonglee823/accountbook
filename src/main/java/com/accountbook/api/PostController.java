package com.accountbook.api;


import com.accountbook.request.PostEdit;
import com.accountbook.request.PostRequest;
import com.accountbook.request.PostSearch;
import com.accountbook.response.PostResponse;
import com.accountbook.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/index")
    public  void index(){

    }

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostRequest request){
        postService.write(request);
    }

    //posts -> 글 전체 조회(검색 + 페이징)
    //posts/{postId} -> 글 한개만 조회
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name ="postId") Long id){
        return postService.get(id);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public PostResponse edit(@PathVariable Long postId, @RequestBody @Valid PostEdit postEdit){
        return postService.edit(postId, postEdit);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId){
        postService.delete(postId);
    }
}