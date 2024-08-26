package com.anupam.Splitwise.exception.user;

import com.anupam.Splitwise.exception.CustomException;
public class UserConflictException extends CustomException {
    public UserConflictException(String msg) {
        super(msg);
    }
}
