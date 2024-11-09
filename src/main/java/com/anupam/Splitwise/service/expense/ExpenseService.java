package com.anupam.Splitwise.service.expense;

import com.anupam.Splitwise.common.SplitwiseConstants;
import com.anupam.Splitwise.converter.expense.ExpenseConverter;
import com.anupam.Splitwise.entity.expense.GroupExpenseEntity;
import com.anupam.Splitwise.handler.expense.ExpenseHandler;
import com.anupam.Splitwise.model.Response;
import com.anupam.Splitwise.model.expense.ExpenseUpdateRequest;
import com.anupam.Splitwise.model.expense.GroupExpense;
import com.anupam.Splitwise.service.audit.AuditService;
import com.anupam.Splitwise.service.settlement.SettlementService;
import com.anupam.Splitwise.validator.expense.ExpenseValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ExpenseService {
    @Autowired
    private ExpenseValidator expenseValidator;
    @Autowired
    private ExpenseConverter expenseConverter;
    @Autowired
    private ExpenseHandler expenseHandler;
    @Autowired
    private SettlementService settlementService;
    @Autowired
    private AuditService auditService;

    public Response addExpense(GroupExpense groupExpense, UUID groupId) throws Exception {
        log.info("started addExpense for group:{}", groupId);
        //validation
        expenseValidator.validateExpense(groupExpense, groupId);
        //converter
        GroupExpenseEntity expenseEntity = expenseConverter.convert(groupExpense);
        //persist
        expenseEntity = expenseHandler.addExpense(expenseEntity, groupId);
        settlementService.calculateSettlementsAsync(groupId);
        auditService.submitAudit(groupExpense, SplitwiseConstants.AuditAction.ADD_EXPENSE,"");
        Response response = new Response(200, expenseEntity);
        log.info("Ended addExpense for group:{}", groupId);
        return response;
    }

    public Response deleteExpense(UUID groupId, UUID expenseId) throws Exception {
        log.info("started ExpenseService::deleteExpense for expenseId:{}", expenseId);
        expenseValidator.validateDeleteExpense(groupId, expenseId);
        expenseHandler.deleteExpense(expenseId);
        Response response = new Response(HttpStatus.NO_CONTENT.value(), "Deleted successfully");
        settlementService.calculateSettlementsAsync(groupId);
        log.info("Ended ExpenseService::deleteExpense for expenseId:{}", expenseId);
        return response;
    }

    public Response updateExpense(UUID groupId, UUID expenseId, ExpenseUpdateRequest updateRequest) throws Exception {
        log.info("started ExpenseService::updateExpense for expenseId:{}", expenseId);
        expenseValidator.validateUpdateExpense(groupId, expenseId,updateRequest);
        GroupExpenseEntity expenseEntity = expenseConverter.convertUpdateRequest(updateRequest);
        expenseEntity = expenseHandler.updateExpense(expenseEntity);
        Response response = new Response(HttpStatus.OK.value(), expenseEntity);
        if(updateRequest.getAmount()!=null){
            settlementService.calculateSettlementsAsync(groupId);
        }
        log.info("Ended ExpenseService::updateExpense for expenseId:{}", expenseId);
        return response;
    }
}
