package com.anupam.Splitwise.exception.group;

import com.anupam.Splitwise.exception.CustomException;

public class InvalidGroupException extends CustomException {
    public InvalidGroupException(String msg){
        super(msg);
    }
}
