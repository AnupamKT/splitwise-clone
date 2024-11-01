package com.anupam.Splitwise.exception.audit;

import com.anupam.Splitwise.exception.CustomException;

public class AuditException extends CustomException {
    public AuditException(String msg){
        super(msg);
    }
}
