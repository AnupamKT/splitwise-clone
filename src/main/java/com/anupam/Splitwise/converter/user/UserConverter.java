package com.anupam.Splitwise.converter.user;

import com.anupam.Splitwise.entity.member.UserEntity;
import com.anupam.Splitwise.model.member.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserConverter {

    public UserEntity convertUser(User user) {
        return UserEntity.
                builder().
                mobileNumber(user.getMobileNumber().trim()).
                email(user.getEmail().trim()).
                password(user.getPassword()).
                name(user.getName().trim()).
                build();
    }
}
