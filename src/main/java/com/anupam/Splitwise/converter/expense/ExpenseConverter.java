package com.anupam.Splitwise.converter.expense;

import com.anupam.Splitwise.entity.expense.GroupExpenseEntity;
import com.anupam.Splitwise.model.expense.ExpenseUpdateRequest;
import com.anupam.Splitwise.model.expense.GroupExpense;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

@Component
public class ExpenseConverter {

    public GroupExpenseEntity convert(GroupExpense expense) {
        return GroupExpenseEntity.
                builder().
                amount(expense.getAmount()).
                description(expense.getDescription()).
                group(expense.getGroup()).
                paidBy(expense.getPaidBy()).
                createdDate(new Date()).
                build();
    }

    public GroupExpenseEntity convertUpdateRequest(ExpenseUpdateRequest updateRequest) {
        GroupExpenseEntity expenseEntity = updateRequest.getExpenseEntity();
        if (updateRequest.getAmount() != null) {
            expenseEntity.setAmount(updateRequest.getAmount());
        }
        if (StringUtils.hasText(updateRequest.getDescription())) {
            expenseEntity.setDescription(updateRequest.getDescription());
        }
        return expenseEntity;
    }
}
