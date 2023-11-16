package com.accountbook.service;


import com.accountbook.domain.Post;
import com.accountbook.domain.PostEditor;
import com.accountbook.exception.PostNotFound;
import com.accountbook.repository.PostRepository;
import com.accountbook.request.PostEdit;
import com.accountbook.request.PostRequest;
import com.accountbook.request.PostSearch;
import com.accountbook.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostRequest postRequest) {

        //Post post = new Post(postRequest.getTitle(), postRequest.getContent());
        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .build();
        postRepository.save(post);
    }

    public PostResponse get(Long id){
        Post post =  postRepository.findById(id)
                .orElseThrow(() -> new PostNotFound());

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle() )
                .content(post.getContent())
                .build();
    }

    public List<PostResponse> getList(PostSearch postsearch){
//        return postRepository.findAll().stream()
//                .map(post -> PostResponse.builder()
//                            .id(post.getId())
//                            .title(post.getTitle())
//                            .content(post.getContent())
//                            .build())
//                .collect(Collectors.toList());
        //Pageable pageable = PageRequest.of(page, 5, Sort.by("id").descending());
        return postRepository.getList(postsearch).stream()
                .map(post -> new PostResponse(post))
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse edit(Long id, PostEdit postEdit){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFound());

        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

        PostEditor postEditor = editorBuilder.title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(postEditor);
        return new PostResponse(post);
    }

    @Transactional
    public void delete(Long id){
        Post post = postRepository.findById(id)
                        .orElseThrow(() -> new PostNotFound());
        postRepository.delete(post);
    }
}