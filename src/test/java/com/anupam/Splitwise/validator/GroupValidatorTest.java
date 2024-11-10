package com.anupam.Splitwise.validator;

import com.anupam.Splitwise.entity.member.UserEntity;
import com.anupam.Splitwise.exception.group.InvalidGroupException;
import com.anupam.Splitwise.handler.user.UserDataHandler;
import com.anupam.Splitwise.model.group.Group;
import com.anupam.Splitwise.util.TestUtil;
import com.anupam.Splitwise.validator.group.GroupValidator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GroupValidatorTest {

    @InjectMocks
    private GroupValidator groupValidator;
    @Mock
    private UserDataHandler userDataHandler;
    private Group group;
    private UserEntity userEntity;
    private static String EMAIL="TestAdminEmail";

    @Test
    public void validateGroupDetailsTest_01() throws Exception {
        group = TestUtil.getGroup();
        userEntity = TestUtil.getUSerEntity();
        Mockito.when(userDataHandler.findUserByEmail(EMAIL)).thenReturn(userEntity);
        UserEntity result = groupValidator.validateGroupDetails(group);
        assertNotNull(result);
        assertEquals(EMAIL,result.getEmail());
    }
    @Test
    public void validateGroupDetailsTest_02() throws Exception {
        group = TestUtil.getGroup();
        Mockito.when(userDataHandler.findUserByEmail(EMAIL)).
                thenThrow(new InvalidGroupException("not a valid admin"));
        Exception exception = assertThrows(InvalidGroupException.class,()->{
            groupValidator.validateGroupDetails(group);
        });
        assertTrue(exception.getMessage().contains("Invalid group details!!"));
    }

    @Test
    public void validateGroupDetailsTest_03(){
        group=null;
        Exception exception=assertThrows(InvalidGroupException.class,()->{
            groupValidator.validateGroupDetails(group);
        });
        assertTrue(exception.getMessage().contains("Group cannot be null or blank"));
    }

    @Test
    public void validateGroupDetailsTest_04(){
        group = TestUtil.getGroup();
        group.setGroupName("");
        Exception exception = assertThrows(InvalidGroupException.class,()->{
        groupValidator.validateGroupDetails(group);
     });
        assertTrue(exception.getMessage().contains("Group name cannot be null or blank"));
    }

    @Test
    public void validateGroupDetailsTest_05(){
        group = TestUtil.getGroup();
        group.setGroupAdminEmail("");
        Exception exception = assertThrows(InvalidGroupException.class,()->{
            groupValidator.validateGroupDetails(group);
        });
        assertTrue(exception.getMessage().contains("Group Admin email cannot be null or blank"));
    }
}
