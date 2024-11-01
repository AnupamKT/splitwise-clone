package com.anupam.Splitwise.exception.audit;

import com.anupam.Splitwise.exception.CustomException;

public class InvalidAuditException extends CustomException {
   public InvalidAuditException(String msg){
       super(msg);
   }
}
