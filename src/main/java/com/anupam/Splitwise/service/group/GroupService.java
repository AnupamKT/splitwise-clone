package com.anupam.Splitwise.service.group;

import com.anupam.Splitwise.converter.group.GroupConverter;
import com.anupam.Splitwise.entity.group.GroupEntity;
import com.anupam.Splitwise.entity.member.UserEntity;
import com.anupam.Splitwise.handler.group.GroupHandler;
import com.anupam.Splitwise.model.Response;
import com.anupam.Splitwise.model.group.Group;
import com.anupam.Splitwise.service.member.MemberService;
import com.anupam.Splitwise.validator.group.GroupValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class GroupService {
    @Autowired
    private GroupHandler groupHandler;
    @Autowired
    private GroupConverter groupConverter;
    @Autowired
    private GroupValidator groupValidator;
    @Autowired
    private MemberService memberService;

    public Response createGroup(Group group) throws Exception {
        String fName = "createGroup";
        log.info("started:{} for group name:{}", fName, group.getGroupName());
        UserEntity userEntity = groupValidator.validateGroupDetails(group);
        GroupEntity groupEntity = groupConverter.convert(group, userEntity);
        groupEntity = groupHandler.createGroup(groupEntity);
        log.info("Ended:{} for group name:{}", fName, group.getGroupName());
        return Response.
                builder().
                status(HttpStatus.ACCEPTED.value()).
                body(groupEntity).
                build();
    }

    public Response getGroup(String groupName) throws Exception {
        String fName = "getGroup";
        log.info("started:{} for group name:{}", fName, groupName);
        GroupEntity groupEntity = groupHandler.findGroupByName(groupName);
        log.info("Ended:{} for group name:{}", fName, groupName);
        return Response.
                builder().
                status(HttpStatus.OK.value()).
                body(groupEntity).
                build();
    }

    @Transactional
    public Response deleteGroup(String groupName) throws Exception {
        String fName = "deleteGroup";
        log.info("started:{} for group name:{}", fName, groupName);
        GroupEntity groupEntity = groupHandler.findGroupByName(groupName);
        groupHandler.deleteGroup(groupEntity);
        log.info("ended:{} for group name:{}", fName, groupName);
        return Response.
                builder().
                status(HttpStatus.NO_CONTENT.value()).
                build();
    }
}
