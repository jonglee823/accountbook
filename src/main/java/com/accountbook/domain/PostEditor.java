package com.accountbook.domain;

import lombok.Builder;
import lombok.Getter;


//수정할 수 있는 필드 정의
@Getter
public class PostEditor {
    private final String title;
    private final String content;

    public PostEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static PostEditorBuilder builder() {
        return new PostEditorBuilder();
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public static class PostEditorBuilder {
        private String title;
        private String content;

        PostEditorBuilder() {
        }

        public PostEditorBuilder title(final String title) {
            this.title = title == null? this.title: title;
            return this;
        }

        public PostEditorBuilder content(final String content) {
            this.content = content == null? this.content: content;
            return this;
        }

        public PostEditor build() {
            return new PostEditor(this.title, this.content);
        }

        public String toString() {
            return "PostEditor.PostEditorBuilder(title=" + this.title + ", content=" + this.content + ")";
        }
    }
}

