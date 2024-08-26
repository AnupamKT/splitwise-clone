package com.anupam.Splitwise.exception.user;

import com.anupam.Splitwise.exception.CustomException;

public class InvalidUserException extends CustomException {
    public InvalidUserException(String msg){
        super(msg);
    }
}
