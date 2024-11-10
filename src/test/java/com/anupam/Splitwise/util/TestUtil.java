package com.anupam.Splitwise.util;

import com.anupam.Splitwise.entity.expense.GroupExpenseEntity;
import com.anupam.Splitwise.entity.group.GroupEntity;
import com.anupam.Splitwise.entity.member.UserEntity;
import com.anupam.Splitwise.model.expense.GroupExpense;
import com.anupam.Splitwise.model.group.Group;

import java.util.UUID;

public class TestUtil {

    private static final String GRP_NAME="Test Group";
    private static final String EMAIL="TestAdminEmail";
    private static final String USERNAME="Test name";
    private static final String EXPENSE_DESC="Test desc";
    private static final String PAID_BY="Test user";
    private static final Double AMOUNT=100.0;
    private static final String USER_EMAIL = "test@gmail.com";
    private static final String USER_NAME = "TestUserName";
    private static final String USER_MOBILE_NUMBER = "911Test111";


    public static Group getGroup(){
       return new Group(GRP_NAME,EMAIL);
    }
    public static UserEntity getUSerEntity(){
      return UserEntity.builder().email(EMAIL).name(USERNAME).build();
    }

    public static GroupEntity getGroupEntity(UUID groupId){
        return GroupEntity.builder().
                groupAdmin(EMAIL).
                groupId(groupId).
                groupName(GRP_NAME).build();
    }

    public static GroupExpense getGroupExpense(){
       return GroupExpense.builder().
               description(EXPENSE_DESC).
               paidBy(PAID_BY).
               amount(AMOUNT).
               build();
    }

    public static GroupExpenseEntity getGroupExpenseEntity(){
        return GroupExpenseEntity.builder().
                amount(AMOUNT).
                paidBy(PAID_BY).
                description(EXPENSE_DESC).
                build();
    }

    public static UserEntity getUserEntity(){
       return UserEntity.builder().
               email(USER_EMAIL).
               name(USER_NAME).
               mobileNumber(USER_MOBILE_NUMBER).
               build();
    }

}
