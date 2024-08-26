package com.anupam.Splitwise.validator.user;

import com.anupam.Splitwise.exception.user.InvalidUserException;
import com.anupam.Splitwise.exception.user.UserConflictException;
import com.anupam.Splitwise.handler.user.UserDataHandler;
import com.anupam.Splitwise.model.member.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class UserValidator {
    @Autowired
    private UserDataHandler dataHandler;

    public void validateCreateUser(User user) throws Exception {
        //validate payload
        validatePayload(user);
        //validate data format--email and mobile and password
        validateUserData(user);
        //validate if user with same email already exists
        validateIfUserExists(user);
    }

    private void validateIfUserExists(User user) throws UserConflictException {
        boolean userCheck = dataHandler.doesUserExists(user.getEmail());
        if(userCheck){
            throw new UserConflictException("User with email "+user.getEmail()+" is already registered");
        }
    }

    private void validateUserData(User user) throws InvalidUserException {
        //email should contain @ symbol
        if (!user.getEmail().contains("@")) {
            throw new InvalidUserException("user email is not in correct format");
        }
        //password should be at least 6 char long
        if (user.getPassword().length() < 6) {
            throw new InvalidUserException("user password is not in correct format");
        }
        //if mobile number is present, it should be 10 digit long
        if (StringUtils.hasText(user.getMobileNumber()) &&
                user.getMobileNumber().length() != 10) {
            throw new InvalidUserException("user mobile number is not in correct format");
        }
    }

    private void validatePayload(User user) throws InvalidUserException {
        if (!StringUtils.hasText(user.getName())) {
            throw new InvalidUserException("user name cannot be null or blank");
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new InvalidUserException("user email cannot be null or blank");
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new InvalidUserException("user password cannot be null or blank");
        }
    }
}
