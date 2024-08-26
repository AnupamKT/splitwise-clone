package com.anupam.Splitwise.model.expense;

import com.anupam.Splitwise.entity.expense.GroupExpenseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseUpdateRequest {

    private Integer amount;
    private String description;
    @JsonIgnore
    private GroupExpenseEntity expenseEntity;
}
