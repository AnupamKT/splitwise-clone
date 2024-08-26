package com.anupam.Splitwise.converter.group;

import com.anupam.Splitwise.entity.group.GroupEntity;
import com.anupam.Splitwise.entity.member.UserEntity;
import com.anupam.Splitwise.model.group.Group;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Component
public class GroupConverter {
    public GroupEntity convert(Group group, UserEntity userEntity) {
        GroupEntity groupEntity = GroupEntity.
                builder().
                groupName(group.getGroupName()).
                groupAdmin(group.getGroupAdminEmail()).
                createdDate(new Date()).
                build();
        groupEntity.setUserEntities(Set.of(userEntity));
        return groupEntity;
    }
}
