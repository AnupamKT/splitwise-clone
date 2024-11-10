package com.anupam.Splitwise.handler;

import com.anupam.Splitwise.entity.expense.GroupExpenseEntity;
import com.anupam.Splitwise.handler.expense.ExpenseHandler;
import com.anupam.Splitwise.repository.expense.ExpenseRepository;
import com.anupam.Splitwise.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ExpenseHandlerTest {

    @InjectMocks
    private ExpenseHandler expenseHandler;
    @Mock
    private ExpenseRepository expenseRepository;
    private GroupExpenseEntity expenseEntity;
    private static final UUID GRP_ID=UUID.randomUUID();
    private static final UUID EXPENSE_ID=UUID.randomUUID();

    @Test
    public void addExpenseTest_01() throws Exception {
        expenseEntity = TestUtil.getGroupExpenseEntity();
        Mockito.when(expenseRepository.saveAndFlush(expenseEntity)).thenReturn(expenseEntity);
        GroupExpenseEntity result = expenseHandler.addExpense(expenseEntity,GRP_ID);
        Mockito.verify(expenseRepository,Mockito.times(1)).saveAndFlush(expenseEntity);
        assertNotNull(result);
        assertEquals(100.0,result.getAmount());
    }

    @Test
    public void addExpenseTest_02(){
        expenseEntity= TestUtil.getGroupExpenseEntity();
        Mockito.when(expenseRepository.saveAndFlush(expenseEntity)).
                thenThrow(new RuntimeException("error occurred"));
        Exception exception = assertThrows(RuntimeException.class,()->{
            expenseHandler.addExpense(expenseEntity,GRP_ID);
        });
        Mockito.verify(expenseRepository,Mockito.times(1)).saveAndFlush(expenseEntity);
        assertEquals("error occurred",exception.getMessage());
    }

    @Test
    public void deleteExpenseTest_01() throws Exception {
        expenseHandler.deleteExpense(EXPENSE_ID);
        Mockito.verify(expenseRepository,Mockito.times(1)).deleteById(EXPENSE_ID);
    }

    @Test
    public void deleteExpenseTest_02() throws Exception {
        Mockito.doThrow(new RuntimeException("error occurred"))
                .when(expenseRepository).deleteById(EXPENSE_ID);
        Exception exception = assertThrows(RuntimeException.class,()->{
            expenseHandler.deleteExpense(EXPENSE_ID);
        });
        Mockito.verify(expenseRepository,Mockito.times(1)).deleteById(EXPENSE_ID);
        assertEquals("error occurred",exception.getMessage());
    }

    @Test
    public void updateExpenseTest_01() throws Exception {
        expenseEntity = TestUtil.getGroupExpenseEntity();
        Mockito.when(expenseRepository.saveAndFlush(expenseEntity)).thenReturn(expenseEntity);
        GroupExpenseEntity result = expenseHandler.updateExpense(expenseEntity);
        Mockito.verify(expenseRepository,Mockito.times(1)).saveAndFlush(expenseEntity);
        assertNotNull(result);
        assertEquals(100.0,result.getAmount());
    }

    @Test
    public void updateExpenseTest_02() throws  Exception{
        expenseEntity= TestUtil.getGroupExpenseEntity();
        Mockito.when(expenseRepository.saveAndFlush(expenseEntity)).
                thenThrow(new RuntimeException("error occurred while updating expense"));
        Exception exception = assertThrows(RuntimeException.class,()->{
            expenseHandler.updateExpense(expenseEntity);
        });
        Mockito.verify(expenseRepository,Mockito.times(1)).saveAndFlush(expenseEntity);
        assertEquals("error occurred while updating expense",exception.getMessage());
    }
}
