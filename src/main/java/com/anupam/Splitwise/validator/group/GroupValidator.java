package com.anupam.Splitwise.validator.group;

import com.anupam.Splitwise.entity.member.UserEntity;
import com.anupam.Splitwise.exception.group.InvalidGroupException;
import com.anupam.Splitwise.handler.user.UserDataHandler;
import com.anupam.Splitwise.model.group.Group;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class GroupValidator {

    @Autowired
    private UserDataHandler userDataHandler;


    public UserEntity validateGroupDetails(Group group) throws InvalidGroupException {
        try {
            validatePayload(group);
            return validateAdminDetails(group);
        } catch (Exception ex) {
            log.error("validation failed for group:{} with error:{}", group.getGroupName(), ex.getMessage());
            throw new InvalidGroupException("Invalid group details!!" + ex.getMessage());
        }
    }

    private UserEntity validateAdminDetails(Group group) throws Exception {
        String email = group.getGroupAdminEmail();
        try {
            return userDataHandler.findUserByEmail(email);
        } catch (Exception ex) {
            String msg = "Email " + email + " is not a registered email!! please register first";
            log.error(msg);
            throw new InvalidGroupException(msg);
        }
    }

    private void validatePayload(Group group) throws InvalidGroupException {
        if (group != null) {
            if (!StringUtils.hasText(group.getGroupName())) {
                throw new InvalidGroupException("Group name cannot be null or blank");
            }
            if (!StringUtils.hasText(group.getGroupAdminEmail())) {
                throw new InvalidGroupException("Group Admin email cannot be null or blank");
            }
        } else {
            throw new InvalidGroupException("Group cannot be null or blank");
        }
    }
}
