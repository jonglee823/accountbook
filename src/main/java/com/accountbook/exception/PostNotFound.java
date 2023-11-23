package com.accountbook.exception;

public class PostNotFound extends AccountException{

    private static final String MESSAGE = "존재 하지 않는 글입니다.";

    public PostNotFound() {
        super(MESSAGE);
    }


    @Override
    public int getStatusCode() {
        return 404;
    }
}