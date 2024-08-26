package com.anupam.Splitwise.exception.expense;

import com.anupam.Splitwise.exception.CustomException;

public class InvalidExpenseDetailsException extends CustomException {

    public InvalidExpenseDetailsException(String msg){
        super(msg);
    }
}
