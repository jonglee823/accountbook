package com.accountbook.api;

import com.accountbook.domain.Post;
import com.accountbook.domain.PostEditor;

import com.accountbook.exception.PostNotFound;
import com.accountbook.repository.PostRepository;
import com.accountbook.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
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

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles(profiles = {"local"})
// @DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @BeforeEach
    void clean(){
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청시 Hello world 출력")
    void test() throws Exception{
        //given
        //Post request = new Post();

        Post request = Post.builder()
                .title("글 제목")
                .content("글 내용")
                .build();

        //ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        //EXPECTED
            mockMvc.perform(post("/posts")
    //MediaType.APPLICATION_FORM_URLENCODED_VALUE
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//                        .param("title","글 제목")
//                        .param("content","글 내용")
                                .contentType(APPLICATION_JSON)
                                .content(requestJson)
                )     //application/json
                        .andExpect(status().isOk())
            .andExpect(content().string(""))
            .andDo(print());
    }
    @Test
    @DisplayName("/posts 요청시 파라미터 valid체크")
    void testValid() throws Exception{
        //given
        Post request = Post.builder().build();
        //ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        //글 내용
        //EXPECTED
        mockMvc.perform(post("/posts")
                                //MediaType.APPLICATION_FORM_URLENCODED_VALUE
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//                        .param("title","글 제목")
//                        .param("content","글 내용")
                                .contentType(APPLICATION_JSON)
                                .content(requestJson)
                )     //application/json
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청 입니다."))
                .andDo(print());

    }

    @Test
    @DisplayName("/posts 요청 내용 DB 저장")
    void test3() throws Exception{

        //given
        Post request = Post.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);
        //글 제목
        //글 내용
        //EXPECTED
        mockMvc.perform(post("/posts")

                                .contentType(APPLICATION_JSON)
                                .content(json)
                )     //application/json
                .andExpect(status().isOk());

        //then
        Assertions.assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);

        Assertions.assertEquals("게시글 제목", post.getTitle());
        Assertions.assertEquals("게시글 내용", post.getContent());
    }

    @Test
    @DisplayName("/posts 여러목록 조회")
    void test4() throws Exception {

        //given
        Post post = Post.builder()
                .title("게시글 제목1")
                .content("게시글 내용1")
                .build();
//        postRepository.save(post);

        Post post1 = Post.builder()
                .title("게시글 제목2")
                .content("게시글 내용2")
                .build();
        //postRepository.save(post1);

        postRepository.saveAll(Lists.newArrayList(post, post1));

        //EXPECTED
        mockMvc.perform(get("/posts")
                        .contentType(APPLICATION_JSON))     //application/json
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].id").value(post1.getId()))
                .andExpect(jsonPath("$[0].title").value(post1.getTitle()))
                .andExpect(jsonPath("$[1].id").value(post.getId()))
                .andExpect(jsonPath("$[1].title").value(post.getTitle()))
                .andDo(print());

    }

    @Test
    @DisplayName("/posts 1page 조회")
    void test5() throws Exception {

        List<Post> requestLIst = IntStream.range(1,31)
                .mapToObj(i-> {
                    return Post.builder()
                            .title("제목 : "+i)
                            .content("내용 : " + i)
                            .build();
                })
                .collect(Collectors.toList());
        postRepository.saveAll(requestLIst);



        //EXPECTED
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))     //application/json
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.length()", is(5)))
                .andDo(print());

    }
    @Test
    @DisplayName("0page 요청시 1page 조회")
    void test6() throws Exception {

        List<Post> requestLIst = IntStream.range(1,31)
                .mapToObj(i-> {
                    return Post.builder()
                            .title("제목 : "+i)
                            .content("내용 : " + i)
                            .build();
                })
                .collect(Collectors.toList());
        postRepository.saveAll(requestLIst);



        //EXPECTED
        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON))     //application/json
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("제목 : 30"))
                .andDo(print());

    }

    @Test
    @DisplayName("글 제목 수정하기")
    void test7() throws Exception {

        Post request = Post.builder()
                            .title("제목")
                            .content("내용")
                            .build();
        postRepository.save(request);

        PostEditor postEditor = PostEditor.builder()
                .title("수정 제목")
                .content("내용")
                .build();

        Post findById =postRepository.findById(request.getId())
                        .orElseThrow(() -> new PostNotFound());

        //EXPECTED
        mockMvc.perform(patch("/posts/{postId}", request.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEditor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("수정 제목"))
                .andDo(print());

    }

    @Test
    @DisplayName("글 삭제하기")
    void deleteById() throws Exception {


        Post request = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(request);

        //EXPECTED
        mockMvc.perform(delete("/posts/{postId}", request.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    @DisplayName("존재하지 않는 데이터 요청시 404 return")
    void notFoundException() throws Exception {

        long id = 1000l;
        //EXPECTED
        mockMvc.perform(get("/posts/{postId}", id)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재 하지 않는 글입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 데이터 수정")
    void updateButNotFoundException() throws Exception {

        long id = 1000l;

        PostEditor postEditor = PostEditor.builder()
                .title("수정 제목")
                .content("내용")
                .build();

        String request = objectMapper.writeValueAsString(postEditor);


        //EXPECTED
        mockMvc.perform(patch("/posts/{postId}", id)
                        .contentType(APPLICATION_JSON)
                        .content(request)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재 하지 않는 글입니다."))
                .andDo(print());
    }


}