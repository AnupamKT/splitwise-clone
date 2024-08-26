package com.anupam.Splitwise.service.member;

import com.anupam.Splitwise.entity.group.GroupEntity;
import com.anupam.Splitwise.handler.group.GroupHandler;
import com.anupam.Splitwise.model.Response;
import com.anupam.Splitwise.validator.member.GroupMemberValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class MemberService {
    @Autowired
    private GroupMemberValidator memberValidator;
    @Autowired
    private GroupHandler groupHandler;

    public Response createGroupMember(UUID groupId, String email) throws Exception {
        log.info("started createGroupMember for groupId:{} for user:{}", groupId, email);
        GroupEntity groupEntity = memberValidator.validateGroupMembers(groupId, email);
        groupEntity = groupHandler.createGroup(groupEntity);
        log.info("ended createGroupMember for groupId:{} for user:{}", groupId, email);
        return new Response(HttpStatus.ACCEPTED.value(), groupEntity);
    }

    public Response removeGroupMember(UUID groupId, String email) throws Exception {
        log.info("started removeGroupMember for groupId:{} for user:{}", groupId, email);
        GroupEntity groupEntity = memberValidator.validateRemoveGroupMembers(groupId, email);
        groupEntity = groupHandler.createGroup(groupEntity);
        log.info("ended removeGroupMember for groupId:{} for user:{}", groupId, email);
        return new Response(HttpStatus.NO_CONTENT.value(), groupEntity);
    }
}
