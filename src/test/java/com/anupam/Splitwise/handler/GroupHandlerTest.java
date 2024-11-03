package com.anupam.Splitwise.handler;

import com.anupam.Splitwise.entity.group.GroupEntity;
import com.anupam.Splitwise.exception.group.GroupNotFoundException;
import com.anupam.Splitwise.handler.group.GroupHandler;
import com.anupam.Splitwise.repository.group.GroupRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class GroupHandlerTest {

    @Mock
    private GroupRepository groupRepository;
    @InjectMocks
    private GroupHandler groupHandler;
    private static GroupEntity groupEntity;
    private static final String GROUP_NAME = "TestGroup";
    private static final String ADMIN_NAME = "TestAdmin";
    private static final UUID GROUP_ID=UUID.randomUUID();


    @BeforeAll
    public static void setup(){
        groupEntity = GroupEntity.builder().
                groupName(GROUP_NAME).
                groupId(GROUP_ID).
                groupAdmin(ADMIN_NAME).build();
    }

    @Test
    public void findGroupByNameTest_o1() throws Exception {
        Mockito.when(groupRepository.findByGroupName(GROUP_NAME)).thenReturn(Optional.of(groupEntity));
        GroupEntity result = groupHandler.findGroupByName(GROUP_NAME);
        Mockito.verify(groupRepository,Mockito.times(1)).findByGroupName(GROUP_NAME);
        assertEquals(groupEntity.getGroupName(),result.getGroupName());
    }
    @Test
    public void findGroupByNameTest_o2(){
        Mockito.when(groupRepository.findByGroupName(GROUP_NAME)).thenReturn(Optional.empty());
        Exception exception = assertThrows(GroupNotFoundException.class,()->{
           groupHandler.findGroupByName(GROUP_NAME);
        });
        Mockito.verify(groupRepository,Mockito.times(1)).findByGroupName(GROUP_NAME);
        String msg = "group details not found with name: " + GROUP_NAME;
        assertEquals(msg,exception.getMessage());
    }
    @Test
    public void findGroupByIdTest_01() throws Exception {
        Mockito.when(groupRepository.findByGroupId(GROUP_ID)).thenReturn(Optional.of(groupEntity));
        GroupEntity result = groupHandler.findGroupById(GROUP_ID);
        Mockito.verify(groupRepository,Mockito.times(1)).findByGroupId(GROUP_ID);
        assertEquals(groupEntity.getGroupName(),result.getGroupName());
        assertEquals(GROUP_ID,result.getGroupId());
    }

    @Test
    public void findGroupByIdTest_02(){
        Mockito.when(groupRepository.findByGroupId(GROUP_ID)).thenReturn(Optional.empty());
        Exception exception = assertThrows(GroupNotFoundException.class,()->{
            groupHandler.findGroupById(GROUP_ID);
        });
        Mockito.verify(groupRepository,Mockito.times(1)).findByGroupId(GROUP_ID);
        String msg = "group details not found with id: " + GROUP_ID;
        assertEquals(msg,exception.getMessage());
    }

    @Test
    public void createGroupTest_01() throws Exception {
        Mockito.when(groupRepository.save(groupEntity)).thenReturn(groupEntity);
        GroupEntity result = groupHandler.createGroup(groupEntity);
        Mockito.verify(groupRepository,Mockito.times(1)).save(groupEntity);
        assertNotNull(groupEntity);
        assertEquals(groupEntity.getGroupName(),result.getGroupName());
    }
    @Test
    public void createGroupTest_02(){
        String msg="error occurred while saving group:"+groupEntity.getGroupName();
        Mockito.when(groupRepository.save(groupEntity)).thenThrow(new RuntimeException(msg));
        Exception exception = assertThrows(RuntimeException.class,()->{
            groupHandler.createGroup(groupEntity);
        });
        Mockito.verify(groupRepository,Mockito.times(1)).save(groupEntity);
        assertEquals(msg,exception.getMessage());
    }

    @Test
    public void deleteGroupTest_01() throws Exception {
        groupHandler.deleteGroup(groupEntity);
        Mockito.verify(groupRepository,Mockito.times(1)).delete(groupEntity);
    }

    @Test
    public void deleteGroupTest_02(){
        String msg="error occurred while deleting group:"+groupEntity.getGroupName();
        doThrow(new RuntimeException(msg)).when(groupRepository).delete(groupEntity);
        Exception exception = assertThrows(RuntimeException.class,()->{
            groupHandler.deleteGroup(groupEntity);
        });
        Mockito.verify(groupRepository,times(1)).delete(groupEntity);
        assertEquals(msg,exception.getMessage());
    }

    @AfterAll
    public static void cleanUp(){
        groupEntity=null;
    }
}
