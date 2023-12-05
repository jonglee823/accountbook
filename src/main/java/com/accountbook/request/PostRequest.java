package com.accountbook.request;

import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class PostRequest {
    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;

    @NotBlank(message = "컨텐츠를 입력해주세요.")
    private String content;

    @Builder
    public PostRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
