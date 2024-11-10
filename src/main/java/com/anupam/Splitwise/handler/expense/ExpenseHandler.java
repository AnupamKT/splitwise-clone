package com.anupam.Splitwise.handler.expense;

import com.anupam.Splitwise.entity.expense.GroupExpenseEntity;
import com.anupam.Splitwise.repository.expense.ExpenseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@Slf4j
public class ExpenseHandler {

    @Autowired
    private ExpenseRepository expenseRepository;

    public GroupExpenseEntity addExpense(GroupExpenseEntity expenseEntity, UUID groupId) throws Exception {
        long startTime = Instant.now().toEpochMilli();
        String expensePaidBy = expenseEntity.getPaidBy();
        log.info("started addExpense for group:{} by member:{}", groupId, expensePaidBy);
        try {
            return expenseRepository.saveAndFlush(expenseEntity);
        } catch (Exception ex) {
            log.error("error occurred while adding expense for group:{} by member:{} with error message:{}", groupId, expensePaidBy, ex.getMessage());
            throw ex;
        } finally {
            long endTime = Instant.now().toEpochMilli();
            long totalTime = endTime - startTime;
            log.info("Ended addExpense for group:{} by member:{}, total time taken is:{} milliseconds", groupId, expensePaidBy, totalTime);
        }
    }

    public void deleteExpense(UUID expenseId) throws Exception {
        log.info("started delete expense for expense id:{}", expenseId);
        long startTime = Instant.now().toEpochMilli();
        try {
            expenseRepository.deleteById(expenseId);
            log.info("successfully deleted expense with id:{}", expenseId);
        } catch (Exception ex) {
            log.error("error occurred while deleting expense with id:{} with error message:{}", expenseId, ex.getMessage());
            throw ex;
        } finally {
            long endTime = Instant.now().toEpochMilli();
            long totalTime = endTime - startTime;
            log.info("ended delete expense for expense id:{},total time taken is:{}", expenseId, totalTime);
        }
    }

    public GroupExpenseEntity updateExpense(GroupExpenseEntity expenseEntity) throws Exception {
        log.info("started updateExpense for:{}", expenseEntity.getExpenseId());
        long startTime = Instant.now().toEpochMilli();
        try {
            expenseEntity = expenseRepository.saveAndFlush(expenseEntity);
            log.info("update expense successful for:{}", expenseEntity.getExpenseId());
        } catch (Exception ex) {
            log.error("error occurred while updating expense for:{} with error message:{}", expenseEntity.getExpenseId(), ex.getMessage());
            throw ex;
        } finally {
            long endTime = Instant.now().toEpochMilli();
            long totalTime = endTime - startTime;
            log.info("Ended updateExpense for:{}, total time taken is:{}", expenseEntity.getExpenseId(), totalTime);
        }
        return expenseEntity;
    }
}
