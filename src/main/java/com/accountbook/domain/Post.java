package com.accountbook.domain;

import com.accountbook.request.PostEdit;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter
public class Post {

    public Post() {
    }

    @Builder
    public Post(String title, String content, User user) {
        this.user = user;
        this.title = title;
        this.content = content;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @ManyToOne
    @JoinColumn
    private User user;

    public PostEditor.PostEditorBuilder toEditor(){
        return PostEditor.builder()
                .title(title)
                .content(content);
    }

    public void edit(PostEditor postEditor){
        title = postEditor.getTitle();
        content = postEditor.getContent();
    }

    public Long getUserId(){
        return this.user.getId();
    }
}
