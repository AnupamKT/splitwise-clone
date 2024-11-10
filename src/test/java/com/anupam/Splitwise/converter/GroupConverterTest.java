package com.anupam.Splitwise.converter;

import com.anupam.Splitwise.converter.group.GroupConverter;
import com.anupam.Splitwise.entity.group.GroupEntity;
import com.anupam.Splitwise.entity.member.UserEntity;
import com.anupam.Splitwise.model.group.Group;
import com.anupam.Splitwise.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GroupConverterTest {

    @InjectMocks
    private GroupConverter converter;

    @Test
    public void convertTest(){
        Group group = TestUtil.getGroup();
        UserEntity userEntity = TestUtil.getUSerEntity();
        GroupEntity result = converter.convert(group,userEntity);
        assertNotNull(result);
        assertEquals(group.getGroupName(),result.getGroupName());
        assertTrue(result.getUserEntities().size()==1);

    }

}
