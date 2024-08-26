package com.anupam.Splitwise.exception.user;

import com.anupam.Splitwise.exception.CustomException;
public class UserNotFoundException extends CustomException {
    public UserNotFoundException(String msg) {
        super(msg);
    }
}
