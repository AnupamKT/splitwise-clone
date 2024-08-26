package com.anupam.Splitwise.validator.member;

import com.anupam.Splitwise.entity.group.GroupEntity;
import com.anupam.Splitwise.entity.member.UserEntity;
import com.anupam.Splitwise.exception.member.InvalidGroupMemberException;
import com.anupam.Splitwise.handler.group.GroupHandler;
import com.anupam.Splitwise.handler.user.UserDataHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class GroupMemberValidator {
    @Autowired
    private GroupHandler groupHandler;
    @Autowired
    private UserDataHandler userDataHandler;

    public GroupEntity validateGroupMembers(UUID groupId, String email) throws Exception {
        log.info("Started validateGroupMembers for groupId:{} and email:{}", groupId, email);
        try {
            //validate if group with given Id exists or not
            GroupEntity groupEntity = groupHandler.findGroupById(groupId);
            //validate if user is registered user or not
            UserEntity userEntity = userDataHandler.findUserByEmail(email);
            //validate if user is already part of the group
            Optional<UserEntity> userEntityOptional = getUserEntityOptional(email, groupEntity);
            if (userEntityOptional.isPresent()) {
                throw new InvalidGroupMemberException("user " + email + " is already added to the group " + groupEntity.getGroupName());
            }
            groupEntity.getUserEntities().add(userEntity);
            log.info("Validation successful for groupId:{} and email:{}", groupId, email);
            return groupEntity;
        } catch (Exception ex) {
            String msg = "Invalid group member details!! " + ex.getMessage();
            log.error(msg);
            throw new InvalidGroupMemberException(msg);
        } finally {
            log.info("ended validateGroupMembers for groupId:{} and email:{}", groupId, email);
        }
    }

    private Optional<UserEntity> getUserEntityOptional(String email, GroupEntity groupEntity) throws InvalidGroupMemberException {
        Optional<UserEntity> userEntityOptional = groupEntity.
                getUserEntities().
                stream().
                filter(userEntity -> userEntity.getEmail().equals(email)).
                findAny();
        return userEntityOptional;
    }

    public GroupEntity validateRemoveGroupMembers(UUID groupId, String email) throws Exception {
        log.info("started validateRemoveGroupMembers for member:{} from group:{}", email, groupId);
        try {
            //validate if group with given Id exists or not
            GroupEntity groupEntity = groupHandler.findGroupById(groupId);
            //cannot allow admin member to be removed from the group
            if (groupEntity.getGroupAdmin().equalsIgnoreCase(email)) {
                throw new InvalidGroupMemberException("Admin cannot be removed from the group!!Please assign another member as admin and try again");
            }
            Optional<UserEntity> userEntityOptional = getUserEntityOptional(email, groupEntity);
            if (!userEntityOptional.isPresent()) {
                String msg = "user with email " + email + "is not part of the group " + groupId;
                log.error(msg);
                throw new InvalidGroupMemberException(msg);
            }
            groupEntity.getUserEntities().remove(userEntityOptional.get());
            return groupEntity;
        } catch (Exception ex) {
            log.error("error occurred while validating remove group members for user:{} from group:{} with error message:{}", email, groupId, ex.getMessage());
            throw ex;
        }
    }
}
