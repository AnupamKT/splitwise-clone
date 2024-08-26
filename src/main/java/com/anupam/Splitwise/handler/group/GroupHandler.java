package com.anupam.Splitwise.handler.group;

import com.anupam.Splitwise.entity.group.GroupEntity;
import com.anupam.Splitwise.exception.group.GroupNotFoundException;
import com.anupam.Splitwise.repository.group.GroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class GroupHandler {

    @Autowired
    private GroupRepository groupRepository;

    public GroupEntity findGroupByName(String groupName) throws Exception {
        try {
            Optional<GroupEntity> groupEntity = groupRepository.findByGroupName(groupName);
            if (groupEntity.isPresent()) {
                return groupEntity.get();
            } else {
                String msg = "group details not found with name: " + groupName;
                log.error(msg);
                throw new GroupNotFoundException(msg);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public GroupEntity findGroupById(UUID groupId) throws Exception {
        try {
            Optional<GroupEntity> groupEntity = groupRepository.findByGroupId(groupId);
            if (groupEntity.isPresent()) {
                return groupEntity.get();
            } else {
                String msg = "group details not found with id: " + groupId;
                log.error(msg);
                throw new GroupNotFoundException(msg);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }
    public GroupEntity createGroup(GroupEntity groupEntity) throws Exception {
        String groupName = groupEntity.getGroupName();
        String groupAdmin = groupEntity.getGroupAdmin();
        try {
            groupEntity = groupRepository.save(groupEntity);
            log.info("group with name:{} is create successfully by:{} ", groupName, groupAdmin);
            return groupEntity;
        } catch (Exception ex) {
            log.error("error occurred while creating a group with name:{} by:{} with error message:{}", groupName, groupAdmin, ex.getMessage());
            throw ex;
        }
    }

    public void deleteGroup(GroupEntity groupEntity) throws Exception{
        String groupName = groupEntity.getGroupName();
        String groupAdmin = groupEntity.getGroupAdmin();
        try{
            groupRepository.delete(groupEntity);
            log.info("group with name:{} is deleted successfully by:{} ", groupName, groupAdmin);
        }catch(Exception ex){
            log.error("error occurred while deleting a group with name:{} by:{} with error message:{}", groupName, groupAdmin, ex.getMessage());
            throw ex;
        }
    }
}
