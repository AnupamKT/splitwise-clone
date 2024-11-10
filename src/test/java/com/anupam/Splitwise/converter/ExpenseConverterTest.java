package com.anupam.Splitwise.converter;

import com.anupam.Splitwise.converter.expense.ExpenseConverter;
import com.anupam.Splitwise.entity.expense.GroupExpenseEntity;
import com.anupam.Splitwise.model.expense.ExpenseUpdateRequest;
import com.anupam.Splitwise.model.expense.GroupExpense;
import com.anupam.Splitwise.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseConverterTest {

    @InjectMocks
    private ExpenseConverter expenseConverter;

    @Test
    public void convertTest(){
         GroupExpense groupExpense = TestUtil.getGroupExpense();
         GroupExpenseEntity result = expenseConverter.convert(groupExpense);
         assertNotNull(result);
         assertEquals(groupExpense.getPaidBy(),result.getPaidBy());
         assertTrue(groupExpense.getAmount()==result.getAmount());
    }

    @Test
    public void convertUpdateRequestTest_01(){
        ExpenseUpdateRequest updateRequest = new ExpenseUpdateRequest();
        GroupExpenseEntity expenseEntity = new GroupExpenseEntity();
        updateRequest.setExpenseEntity(expenseEntity);
        updateRequest.setAmount(10.0);
        GroupExpenseEntity result = expenseConverter.convertUpdateRequest(updateRequest);
        assertNotNull(result);
        assertEquals(updateRequest.getAmount(),result.getAmount());
        assertTrue(result.getDescription()==null);
    }

    @Test
    public void convertUpdateRequestTest_02(){
        ExpenseUpdateRequest updateRequest = new ExpenseUpdateRequest();
        GroupExpenseEntity expenseEntity = new GroupExpenseEntity();
        updateRequest.setExpenseEntity(expenseEntity);
        updateRequest.setDescription("Test desc");
        GroupExpenseEntity result = expenseConverter.convertUpdateRequest(updateRequest);
        assertNotNull(result);
        assertNotNull(result.getDescription());
        assertTrue(result.getAmount()==null);
    }

    @Test
    public void convertUpdateRequestTest_03(){
        ExpenseUpdateRequest updateRequest = new ExpenseUpdateRequest();
        GroupExpenseEntity expenseEntity = new GroupExpenseEntity();
        updateRequest.setExpenseEntity(expenseEntity);
        updateRequest.setDescription("Test desc");
        updateRequest.setAmount(10.0);
        GroupExpenseEntity result = expenseConverter.convertUpdateRequest(updateRequest);
        assertNotNull(result);
        assertNotNull(result.getDescription());
        assertNotNull(result.getAmount());
        assertEquals(10.0,result.getAmount());
    }

}
