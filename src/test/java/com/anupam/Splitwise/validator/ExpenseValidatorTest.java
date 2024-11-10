package com.anupam.Splitwise.validator;

import com.anupam.Splitwise.entity.expense.GroupExpenseEntity;
import com.anupam.Splitwise.entity.group.GroupEntity;
import com.anupam.Splitwise.entity.member.UserEntity;
import com.anupam.Splitwise.exception.expense.InvalidExpenseDetailsException;
import com.anupam.Splitwise.exception.group.GroupNotFoundException;
import com.anupam.Splitwise.handler.group.GroupHandler;
import com.anupam.Splitwise.model.expense.ExpenseUpdateRequest;
import com.anupam.Splitwise.model.expense.GroupExpense;
import com.anupam.Splitwise.util.TestUtil;
import com.anupam.Splitwise.validator.expense.ExpenseValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseValidatorTest {

    @InjectMocks
    private ExpenseValidator expenseValidator;
    @Mock
    private GroupHandler groupHandler;
    private GroupExpense groupExpense;
    private GroupEntity groupEntity;
    private ExpenseUpdateRequest updateRequest;
    private static final UUID GRP_ID = UUID.randomUUID();
    private static final UUID EXPENSE_ID = UUID.randomUUID();

    @Test
    public void validateExpenseTest_01() throws Exception {
        groupExpense = TestUtil.getGroupExpense();
        groupEntity = TestUtil.getGroupEntity(GRP_ID);
        groupEntity.setUserEntities(Set.of(UserEntity.builder().email("Test user").build()));
        Mockito.when(groupHandler.findGroupById(GRP_ID)).thenReturn(groupEntity);
        expenseValidator.validateExpense(groupExpense, GRP_ID);
        Mockito.verify(groupHandler, Mockito.times(1)).findGroupById(GRP_ID);
        assertEquals(groupEntity.getGroupName(), groupExpense.getGroup().getGroupName());
    }

    @Test
    public void validateExpenseTest_02() throws Exception {
        groupExpense = TestUtil.getGroupExpense();
        Mockito.when(groupHandler.findGroupById(GRP_ID)).thenThrow(new GroupNotFoundException("Group not found exception"));
        Exception exception = assertThrows(InvalidExpenseDetailsException.class, () -> {
            expenseValidator.validateExpense(groupExpense, GRP_ID);
        });
        assertTrue(exception.getMessage().contains("Group not found exception"));
    }

    @Test
    public void validateExpenseTest_03() throws Exception {
        groupExpense = TestUtil.getGroupExpense();
        groupEntity = TestUtil.getGroupEntity(GRP_ID);
        groupEntity.setUserEntities(Set.of(UserEntity.builder().email("Test user").build()));
        Mockito.when(groupHandler.findGroupById(GRP_ID)).thenReturn(groupEntity);
        groupExpense.setPaidBy("Invalid user");
        Exception exception = assertThrows(InvalidExpenseDetailsException.class, () -> {
            expenseValidator.validateExpense(groupExpense, GRP_ID);
        });
        assertTrue(exception.getMessage().contains("member is not part of the group"));
    }

    @Test
    public void validateExpenseTest_04() throws Exception {
        groupExpense = TestUtil.getGroupExpense();
        groupExpense.setAmount(null);
        Exception exception = assertThrows(InvalidExpenseDetailsException.class, () -> {
            expenseValidator.validateExpense(groupExpense, GRP_ID);
        });
        assertTrue(exception.getMessage().contains("amount cannot be null or blank"));
    }

    @Test
    public void validateExpenseTest_05() throws Exception {
        groupExpense = TestUtil.getGroupExpense();
        groupExpense.setAmount(0.0);
        Exception exception = assertThrows(InvalidExpenseDetailsException.class, () -> {
            expenseValidator.validateExpense(groupExpense, GRP_ID);
        });
        assertTrue(exception.getMessage().contains("amount must be greater than zero"));
    }

    @Test
    public void validateExpenseTest_06() throws Exception {
        groupExpense = TestUtil.getGroupExpense();
        groupExpense.setPaidBy("");
        Exception exception = assertThrows(InvalidExpenseDetailsException.class, () -> {
            expenseValidator.validateExpense(groupExpense, GRP_ID);
        });
        assertTrue(exception.getMessage().contains("Paid by cannot be null or blank"));
    }

    @Test
    public void validateDeleteExpenseTest_01() throws Exception {
        groupEntity = TestUtil.getGroupEntity(GRP_ID);
        groupEntity.setExpenses(Set.of(GroupExpenseEntity.builder().expenseId(EXPENSE_ID).build()));
        Mockito.when(groupHandler.findGroupById(GRP_ID)).thenReturn(groupEntity);
        expenseValidator.validateDeleteExpense(GRP_ID, EXPENSE_ID);
        Mockito.verify(groupHandler, Mockito.times(1)).findGroupById(GRP_ID);
    }

    @Test
    public void validateDeleteExpenseTest_02() throws Exception {
        Mockito.when(groupHandler.findGroupById(GRP_ID)).
                thenThrow(new GroupNotFoundException("group not found"));
        Exception exception = assertThrows(InvalidExpenseDetailsException.class, () -> {
            expenseValidator.validateDeleteExpense(GRP_ID, EXPENSE_ID);
        });
        assertTrue(exception.getMessage().contains("group not found"));
    }

    @Test
    public void validateDeleteExpenseTest_03() throws Exception {
        groupEntity = TestUtil.getGroupEntity(GRP_ID);
        groupEntity.setExpenses(Set.of(GroupExpenseEntity.builder().expenseId(EXPENSE_ID).build()));
        Mockito.when(groupHandler.findGroupById(GRP_ID)).thenReturn(groupEntity);
        Exception exception = assertThrows(InvalidExpenseDetailsException.class, () -> {
            expenseValidator.validateDeleteExpense(GRP_ID, UUID.randomUUID());
        });
        assertTrue(exception.getMessage().contains("not part of the group with Id"));
    }

    @Test
    public void validateUpdateExpenseTest_01() throws Exception {
        updateRequest = new ExpenseUpdateRequest();
        groupEntity = TestUtil.getGroupEntity(GRP_ID);
        //diff amount to pass update validation
        // will fail if amount  and description both are same
        updateRequest.setAmount(200.0);
        updateRequest.setDescription("Test description");
        groupEntity.setExpenses(Set.of(GroupExpenseEntity.builder().
                expenseId(EXPENSE_ID).
                description("Test description").
                amount(100.0).build()));
        Mockito.when(groupHandler.findGroupById(GRP_ID)).thenReturn(groupEntity);
        expenseValidator.validateUpdateExpense(GRP_ID, EXPENSE_ID, updateRequest);
        Mockito.verify(groupHandler, Mockito.times(1)).findGroupById(GRP_ID);
    }

    @Test
    public void validateUpdateExpenseTest_02() throws Exception {
        updateRequest = new ExpenseUpdateRequest();
        updateRequest.setAmount(200.0);
        updateRequest.setDescription("Test description");
        String exceptionMsg = "Group not found";
        Mockito.when(groupHandler.findGroupById(GRP_ID)).thenThrow(new GroupNotFoundException(exceptionMsg));
        Exception exception = assertThrows(InvalidExpenseDetailsException.class, () -> {
            expenseValidator.validateUpdateExpense(GRP_ID, EXPENSE_ID, updateRequest);
        });
        assertTrue(exception.getMessage().contains(exceptionMsg));
    }

    @Test
    public void validateUpdateExpenseTest_03() throws Exception {
        updateRequest = new ExpenseUpdateRequest();
        groupEntity = TestUtil.getGroupEntity(GRP_ID);
        updateRequest.setAmount(200.0);
        updateRequest.setDescription("Test description");

        groupEntity.setExpenses(Set.of(GroupExpenseEntity.builder().
                expenseId(EXPENSE_ID).
                description("Test description").
                amount(100.0).build()));

        Mockito.when(groupHandler.findGroupById(GRP_ID)).thenReturn(groupEntity);
        Exception exception = assertThrows(InvalidExpenseDetailsException.class, () -> {
            expenseValidator.validateUpdateExpense(GRP_ID, UUID.randomUUID(), updateRequest);
        });
        assertTrue(exception.getMessage().contains("Expense with ID"));
        assertTrue(exception.getMessage().contains("not part of group with ID"));
    }

    @Test
    public void validateUpdateExpenseTest_04() throws Exception {
        updateRequest = new ExpenseUpdateRequest();
        groupEntity = TestUtil.getGroupEntity(GRP_ID);
        //setting same amount and description to get the exception
        updateRequest.setAmount(100.0);
        updateRequest.setDescription("Test description");
        groupEntity.setExpenses(Set.of(GroupExpenseEntity.builder().
                expenseId(EXPENSE_ID).
                description("Test description").
                amount(100.0).build()));
        Mockito.when(groupHandler.findGroupById(GRP_ID)).thenReturn(groupEntity);
        Exception exception = assertThrows(InvalidExpenseDetailsException.class, () -> {
            expenseValidator.validateUpdateExpense(GRP_ID, EXPENSE_ID, updateRequest);
        });
        Mockito.verify(groupHandler, Mockito.times(1)).findGroupById(GRP_ID);
        assertTrue(exception.getMessage().contains("found nothing to update for expenseId"));
    }

    @Test
    public void validateUpdateExpenseTest_05() throws Exception {
        updateRequest = new ExpenseUpdateRequest();
        groupEntity = TestUtil.getGroupEntity(GRP_ID);

        updateRequest.setAmount(-1.0);
        updateRequest.setDescription("Test description");
        groupEntity.setExpenses(Set.of(GroupExpenseEntity.builder().
                expenseId(EXPENSE_ID).
                description("Test description").
                amount(100.0).build()));
        Mockito.when(groupHandler.findGroupById(GRP_ID)).thenReturn(groupEntity);
        Exception exception = assertThrows(InvalidExpenseDetailsException.class, () -> {
            expenseValidator.validateUpdateExpense(GRP_ID, EXPENSE_ID, updateRequest);
        });
        Mockito.verify(groupHandler, Mockito.times(1)).findGroupById(GRP_ID);
        assertTrue(exception.getMessage().contains("amount must be greater than zero"));
    }
}
