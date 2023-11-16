package com.accountbook.api;

import com.accountbook.domain.Post;
import com.accountbook.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles(profiles = {"local"})
@SpringBootTest
public class PostContollerDocTest {

    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                                                        .uris().withScheme("https")
                                                        .withHost("accoutbook.com").withPort(443)
                )
                .build();
    }

    @BeforeEach
    void clean(){
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("단 건 조회")
    void selectOne() throws Exception {

        //given
        Post post = Post.builder()
                .title("게시글 제목")
                .content("게시글 내용")
                .build();


        //when
        postRepository.save(post);

        this.mockMvc.perform(get("/posts/{postId}", 1L)
                            .accept(MediaType.APPLICATION_JSON))
                            .andDo(document("post-inquiry"
                                    , pathParameters(
                                                parameterWithName("postId").description("게시글 ID")
                                    ),
                                    responseFields(
                                            fieldWithPath("id").description("게시글 ID"),
                                            fieldWithPath("title").description("게시글 제목"),
                                            fieldWithPath("content").description("게시글 내용")
                                    )

                            ))
                .andDo(print())
                            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 저장")
    void insertPost() throws Exception{

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
        this.mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json)
                )     //application/json
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("post-create"
                        ,requestFields(
                                fieldWithPath("id").description("게시글 ID")
                                ,fieldWithPath("title").description("게시글 제목")
                                ,fieldWithPath("content").description("게시글 내용").optional()
                        )
                ));
    }
}