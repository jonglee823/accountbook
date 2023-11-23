package com.accountbook.exception;

public class InvalidLoginInfo extends AccountException{

    private static final String MESSAGE = "Invalid ID or PW.";

    public InvalidLoginInfo(){
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
