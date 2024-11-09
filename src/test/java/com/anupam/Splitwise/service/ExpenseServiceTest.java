package com.anupam.Splitwise.service;

import com.anupam.Splitwise.common.SplitwiseConstants;
import com.anupam.Splitwise.converter.expense.ExpenseConverter;
import com.anupam.Splitwise.entity.expense.GroupExpenseEntity;
import com.anupam.Splitwise.exception.expense.InvalidExpenseDetailsException;
import com.anupam.Splitwise.handler.expense.ExpenseHandler;
import com.anupam.Splitwise.model.Response;
import com.anupam.Splitwise.model.expense.ExpenseUpdateRequest;
import com.anupam.Splitwise.model.expense.GroupExpense;
import com.anupam.Splitwise.service.audit.AuditService;
import com.anupam.Splitwise.service.expense.ExpenseService;
import com.anupam.Splitwise.service.settlement.SettlementService;
import com.anupam.Splitwise.validator.expense.ExpenseValidator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ExpenseServiceTest {

    @InjectMocks
    private ExpenseService expenseService;
    @Mock
    private ExpenseValidator expenseValidator;
    @Mock
    private ExpenseConverter expenseConverter;
    @Mock
    private ExpenseHandler expenseHandler;
    @Mock
    private SettlementService settlementService;
    @Mock
    private AuditService auditService;
    private static GroupExpense groupExpense;
    private static GroupExpenseEntity expenseEntity;
    private static ExpenseUpdateRequest updateRequest;
    private final UUID GROUP_ID = UUID.randomUUID();
    private final UUID EXPENSE_ID = UUID.randomUUID();

    @BeforeAll
    public static void setup() {
        groupExpense = GroupExpense.builder().
                amount(100.0).
                paidBy("Test user").
                description("Test desc").build();
        expenseEntity = GroupExpenseEntity.builder().
                amount(100.0).
                paidBy("Test User").
                description("Test").build();
        updateRequest = new ExpenseUpdateRequest();

    }

    @Test
    public void addExpenseTest_01() throws Exception {
        Mockito.when(expenseConverter.convert(groupExpense)).thenReturn(expenseEntity);
        Mockito.when(expenseHandler.addExpense(expenseEntity, GROUP_ID)).thenReturn(expenseEntity);
        Response response = expenseService.addExpense(groupExpense, GROUP_ID);
        Mockito.verify(expenseValidator, Mockito.times(1)).validateExpense(groupExpense, GROUP_ID);
        Mockito.verify(settlementService, Mockito.times(1)).calculateSettlementsAsync(GROUP_ID);
        Mockito.verify(auditService, Mockito.times(1)).
                submitAudit(groupExpense, SplitwiseConstants.AuditAction.ADD_EXPENSE, "");
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals("Test User", expenseEntity.getPaidBy());
    }

    @Test
    public void addExpenseTest_02() throws Exception {

        doThrow(new InvalidExpenseDetailsException("Invalid expense details!!")).
                when(expenseValidator).validateExpense(groupExpense, GROUP_ID);
        Exception exception = assertThrows(InvalidExpenseDetailsException.class, () -> {
            expenseService.addExpense(groupExpense, GROUP_ID);
        });
        assertEquals("Invalid expense details!!", exception.getMessage());
    }

    @Test
    public void addExpenseTest_03() throws Exception {
        Mockito.when(expenseConverter.convert(groupExpense)).thenReturn(expenseEntity);
        Mockito.when(expenseHandler.addExpense(expenseEntity, GROUP_ID)).
                thenThrow(new RuntimeException("error occurred while adding expense"));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            expenseService.addExpense(groupExpense, GROUP_ID);
        });
        Mockito.verify(expenseValidator, times(1)).validateExpense(groupExpense, GROUP_ID);
        assertEquals("error occurred while adding expense", exception.getMessage());
    }

    @Test
    public void deleteExpenseTest_01() throws Exception {
        Response response = expenseService.deleteExpense(GROUP_ID, EXPENSE_ID);
        Mockito.verify(expenseValidator, times(1)).validateDeleteExpense(GROUP_ID, EXPENSE_ID);
        Mockito.verify(expenseHandler, times(1)).deleteExpense(EXPENSE_ID);
        Mockito.verify(settlementService, times(1)).calculateSettlementsAsync(GROUP_ID);
        assertNotNull(response);
        assertEquals(204, response.getStatus());
    }

    @Test
    public void deleteExpenseTest_02() throws InvalidExpenseDetailsException {
        doThrow(new InvalidExpenseDetailsException("Invalid delete expense details!!")).
                when(expenseValidator).validateDeleteExpense(GROUP_ID, EXPENSE_ID);
        Exception exception = assertThrows(InvalidExpenseDetailsException.class, () -> {
            expenseService.deleteExpense(GROUP_ID, EXPENSE_ID);
        });
        assertEquals("Invalid delete expense details!!", exception.getMessage());
    }

    @Test
    public void deleteExpenseTest_03() throws Exception {
        doThrow(new InvalidExpenseDetailsException("error occurred while delete expense")).
                when(expenseHandler).deleteExpense(EXPENSE_ID);
        Exception exception = assertThrows(InvalidExpenseDetailsException.class, () -> {
            expenseService.deleteExpense(GROUP_ID, EXPENSE_ID);
        });
        Mockito.verify(expenseValidator, times(1)).validateDeleteExpense(GROUP_ID, EXPENSE_ID);
        assertEquals("error occurred while delete expense", exception.getMessage());
    }

    @Test
    public void updateExpenseTest_01() throws Exception {
        updateRequest.setDescription("Test desc");
        updateRequest.setAmount(100.0);
        Mockito.when(expenseConverter.convertUpdateRequest(updateRequest)).thenReturn(expenseEntity);
        Mockito.when(expenseHandler.updateExpense(expenseEntity)).thenReturn(expenseEntity);
        Response response = expenseService.updateExpense(GROUP_ID, EXPENSE_ID, updateRequest);
        Mockito.verify(settlementService, times(1)).calculateSettlementsAsync(GROUP_ID);
        assertNotNull(response);
        assertEquals(200, response.getStatus());
    }

    @Test
    public void updateExpenseTest_02() throws Exception {
        updateRequest.setDescription("Test desc");
        Mockito.when(expenseConverter.convertUpdateRequest(updateRequest)).thenReturn(expenseEntity);
        Mockito.when(expenseHandler.updateExpense(expenseEntity)).thenReturn(expenseEntity);
        Response response = expenseService.updateExpense(GROUP_ID, EXPENSE_ID, updateRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatus());
    }

    @Test
    public void updateExpenseTest_03() throws InvalidExpenseDetailsException {
        updateRequest.setAmount(100.0);
        updateRequest.setDescription("Test desc");
        doThrow(new InvalidExpenseDetailsException("error occurred while validating update expense")).
                when(expenseValidator).validateUpdateExpense(GROUP_ID, EXPENSE_ID, updateRequest);
        Exception exception = assertThrows(InvalidExpenseDetailsException.class, () -> {
            expenseService.updateExpense(GROUP_ID, EXPENSE_ID, updateRequest);
        });
        assertEquals("error occurred while validating update expense", exception.getMessage());
    }

    @Test
    public void updateExpenseTest_04() throws InvalidExpenseDetailsException {
        updateRequest.setAmount(100.0);
        updateRequest.setDescription("Test desc");
        Mockito.when(expenseConverter.convertUpdateRequest(updateRequest)).thenReturn(expenseEntity);
        Mockito.when(expenseHandler.updateExpense(expenseEntity)).
                thenThrow(new RuntimeException("error occurred while updating expense"));
        Exception exception = assertThrows(RuntimeException.class, () -> {
            expenseService.updateExpense(GROUP_ID, EXPENSE_ID, updateRequest);
        });
        Mockito.verify(expenseValidator, times(1)).
                validateUpdateExpense(GROUP_ID, EXPENSE_ID, updateRequest);
        assertEquals("error occurred while updating expense", exception.getMessage());
    }

    @AfterAll
    public static void cleanUp() {
        groupExpense = null;
        expenseEntity = null;
        updateRequest = null;
    }

}
