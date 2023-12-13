package com.accountbook.service;

import com.accountbook.config.AccountMockUser;
import com.accountbook.config.UserPrincipal;
import com.accountbook.domain.Post;
import com.accountbook.domain.User;
import com.accountbook.exception.PostNotFound;
import com.accountbook.repository.PostRepository;
import com.accountbook.repository.UserRepository;
import com.accountbook.request.PostEdit;
import com.accountbook.request.PostRequest;
import com.accountbook.request.PostSearch;
import com.accountbook.response.PostResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles(profiles = {"local"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostService postService;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }


    @AfterEach
    void cleanUser() {
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("글 작성")
    @AccountMockUser
    void test1() {

        User user = userRepository.findAll().get(0);

        //given
        Post post = Post.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .user(user)
                .build();

        //when
        postRepository.save(post);

        //then
        assertEquals(1L, postRepository.count());
        Post findPost = postRepository.findAll().get(0);
        assertEquals("게시글 제목", findPost.getTitle());
        assertEquals("게시글 내용", findPost.getContent());
    }

    @Test
    @DisplayName("글 한개 조회")
    void selectPostOne() {
        //given
        Post post = Post.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .build();

        //when
        postRepository.save(post);

        //when
        PostResponse findPost = postService.get(post.getId());

        //then
        assertNotNull(findPost);
        assertEquals(findPost.getId(), post.getId());
        assertEquals("게시글 제목", findPost.getTitle());
        assertEquals("게시글 내용", findPost.getContent());
    }


    @Test
    @DisplayName("글 한개 조회 use Get Request")
    void selectPostOneUseGetRequest() throws Exception {
        //given
        Post post = Post.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .build();

        postRepository.save(post);

        //when

        //Post post = postService.get(postId);
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                )     //application/jsonpost
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("게시글 제목"))
                .andExpect(jsonPath("$.content").value("게시글 내용"))
                .andDo(print());


        //then
        //Assertions.assertNotNull(post);
        //Assertions.assertEquals(1L, postRepository.count());
        //Assertions.assertEquals("게시글 제목", post.getTitle());
        //Assertions.assertEquals("게시글 내용", post.getContent());
    }
//
//    @Test
//    @DisplayName("글 여러개 조회 use Get Request")
//    void selectMultiRowUseGetRequest() throws Exception {
//        //given
//        Post post = Post.builder()
//                .title("게시글 제목1")
//                .content("게시글 내용1")
//                .build();
//        postRepository.save(post);
//
//        Post post1 = Post.builder()
//                .title("게시글 제목2")
//                .content("게시글 내용2")
//                .build();
//        postRepository.save(post1);
//
//        //when
//        List<PostResponse> postList = postService.getList(1);
//
//        assertEquals(2L, postList.size());
//
//    }

    @Test
    @DisplayName("글 1페이지 조회")
    void select1PageGetRequest() throws Exception {

        List<Post> requestLIst = IntStream.range(0, 20)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("제목 : " + i)
                            .content("내용 : " + i)
                            .build();
                })
                .collect(Collectors.toList());

        postRepository.saveAll(requestLIst);

//        Pageable pageable = PageRequest.of(0, 5);
        PostSearch postsearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();

        //when
        List<PostResponse> postList = postService.getList(postsearch);

        assertEquals(10L, postList.size());
        assertEquals("제목 : 19", postList.get(0).getTitle());

    }

    @Test
    @DisplayName("글 제목 수정")
    void updateTitle() throws Exception {
        //given
        Post request = Post.builder()
                .title("제목 : ")
                .content("내용 : ")
                .build();

        postRepository.save(request);

        //when
        PostEdit postEdit = PostEdit.builder()
                .title("수정 제목")
                //.content("내용 : ")
                .build();
        postService.edit(request.getId(), postEdit);

        //then
        Post changedPost = postRepository.findById(request.getId())
                .orElseThrow(() -> new PostNotFound());
        assertEquals("수정 제목", changedPost.getTitle());
        assertEquals("내용 : ", changedPost.getContent());
    }

    @Test
    @DisplayName("글 내용 수정")
    void updateContents() throws Exception {
        //given
        Post request = Post.builder()
                .title("제목 : ")
                .content("내용 : ")
                .build();

        postRepository.save(request);

        //when
        PostEdit postEdit = PostEdit.builder()
                //.title("제목 : ")
                .content("수정 내용")
                .build();
        postService.edit(request.getId(), postEdit);

        //then
        Post changedPost = postRepository.findById(request.getId())
                .orElseThrow(() -> new PostNotFound());
        assertEquals("제목 : ", changedPost.getTitle());
        assertEquals("수정 내용", changedPost.getContent());
    }

    @Test
    @DisplayName("글 삭제")
    void deleteById() {

        //given
        Post request = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(request);

        //when
        postService.delete(request.getId());

        assertEquals(0, postRepository.count());

    }

    @Test
    @DisplayName("글 한개 조회(실패케이스)")
    void selectPostOneFailed() {
        //given
        Post postRequest = Post.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .build();

        //when
        postRepository.save(postRequest);

        //when
        PostNotFound e = assertThrows(PostNotFound.class, () -> {
            postService.get(postRequest.getId() + 1L);
        });


    }

    @Test
    @DisplayName("글 삭제 (실패케이스)")
    void failedDelete() {
        //given
        Post postRequest = Post.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .build();
        postRepository.save(postRequest);

        //when
        assertThrows(PostNotFound.class, () -> {
            postService.delete(postRequest.getId() + 100);
        });

    }
}