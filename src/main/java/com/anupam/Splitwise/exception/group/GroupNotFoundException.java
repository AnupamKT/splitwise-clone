package com.anupam.Splitwise.exception.group;

import com.anupam.Splitwise.exception.CustomException;

public class GroupNotFoundException extends CustomException {
    public GroupNotFoundException(String msg) {
        super(msg);
    }
}
