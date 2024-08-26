package com.anupam.Splitwise.exception.member;

import com.anupam.Splitwise.exception.CustomException;

public class InvalidGroupMemberException extends CustomException {

    public InvalidGroupMemberException(String msg){
        super(msg);
    }
}
