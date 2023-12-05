package com.accountbook.exception;

public class AlreadyExistsEmailException extends AccountException{

    private static final String MESSAGE = "이미 가입된 ID 입니다.";

    public AlreadyExistsEmailException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
