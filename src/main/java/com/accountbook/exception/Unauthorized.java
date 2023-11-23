package com.accountbook.exception;

public class Unauthorized extends AccountException{

    private static final String MESSAGE = "인증이 필요 합니다.";

    public Unauthorized() {
        super(MESSAGE);
    }

    public Unauthorized(String message, Throwable cause) {
        super(message, cause);
    }


    @Override
    public int getStatusCode() {
        return 401;
    }
}
