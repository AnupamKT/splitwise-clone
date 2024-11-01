package com.anupam.Splitwise.validator.expense;

import com.anupam.Splitwise.entity.expense.GroupExpenseEntity;
import com.anupam.Splitwise.entity.group.GroupEntity;
import com.anupam.Splitwise.entity.member.UserEntity;
import com.anupam.Splitwise.exception.expense.InvalidExpenseDetailsException;
import com.anupam.Splitwise.handler.group.GroupHandler;
import com.anupam.Splitwise.model.expense.ExpenseUpdateRequest;
import com.anupam.Splitwise.model.expense.GroupExpense;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
public class ExpenseValidator {
    @Autowired
    private GroupHandler groupHandler;

    public void validateExpense(GroupExpense groupExpense, UUID groupId) throws Exception {
        log.info("started validateExpense for group:{}", groupId);
        try {
            validateExpenseDetails(groupExpense);
            GroupEntity groupEntity = groupHandler.findGroupById(groupId);
            validateIfMemberIsPartOfGroup(groupExpense, groupEntity);
            groupExpense.setGroup(groupEntity);
        } catch (Exception ex) {
            log.error("error occurred while validating add expense for group:{} with error message:{}", groupId, ex.getMessage());
            throw new InvalidExpenseDetailsException("Invalid expense details!! " + ex.getMessage());
        }
        log.info("Ended validateExpense for group:{}", groupId);
    }

    private void validateExpenseDetails(GroupExpense groupExpense) throws InvalidExpenseDetailsException {
        if (groupExpense.getAmount() == null) {
            throw new InvalidExpenseDetailsException("amount cannot be null or blank");
        }
        if (groupExpense.getAmount() <= 0) {
            throw new InvalidExpenseDetailsException("amount must be greater than zero");
        }
        if (!StringUtils.hasText(groupExpense.getPaidBy())) {
            throw new InvalidExpenseDetailsException("Paid by cannot be null or blank");
        }
    }

    private void validateIfMemberIsPartOfGroup(GroupExpense groupExpense, GroupEntity groupEntity) throws InvalidExpenseDetailsException {
        Optional<UserEntity> userEntityOptional = groupEntity.
                getUserEntities().
                stream().
                filter(userEntity -> userEntity.getEmail().equalsIgnoreCase(groupExpense.getPaidBy())).
                findAny();
        if (!userEntityOptional.isPresent()) {
            throw new InvalidExpenseDetailsException("member is not part of the group");
        }
    }

    public void validateDeleteExpense(UUID groupId, UUID expenseId) throws InvalidExpenseDetailsException {
        try {
            GroupEntity groupEntity = groupHandler.findGroupById(groupId);
            Optional<GroupExpenseEntity> optional = groupEntity.
                    getExpenses().
                    stream().
                    filter(expenseEntity -> expenseEntity.getExpenseId().equals(expenseId)).findAny();
            if (optional.isEmpty()) {
                String msg = "expense details with id " + expenseId + " not part of the group with Id " + groupId;
                log.error("error occurred while validating delete expense details with error:{}", msg);
                throw new InvalidExpenseDetailsException(msg);
            }
        } catch (Exception ex) {
            log.error("error occurred while validating delete expense with error message:{}", ex.getMessage());
            throw new InvalidExpenseDetailsException("Invalid delete expense details!!" + ex.getMessage());
        }
    }

    public void validateUpdateExpense(UUID groupId, UUID expenseId, ExpenseUpdateRequest updateRequest) throws InvalidExpenseDetailsException {
        try {
            GroupEntity groupEntity = groupHandler.findGroupById(groupId);
            GroupExpenseEntity expenseEntity = findExpenseById(groupEntity, expenseId);
            validateUpdateRequest(expenseEntity, updateRequest);
            updateRequest.setExpenseEntity(expenseEntity);
        } catch (Exception ex) {
            String errorMsg = "error occurred while validating update expense " + ex.getMessage();
            log.error(errorMsg);
            throw new InvalidExpenseDetailsException(errorMsg);
        }
    }

    private GroupExpenseEntity findExpenseById(GroupEntity groupEntity, UUID expenseId) throws InvalidExpenseDetailsException {
        return groupEntity.getExpenses().stream()
                .filter(expenseEntity -> expenseEntity.getExpenseId().equals(expenseId))
                .findFirst()
                .orElseThrow(() -> {
                    String msg = String.format("Expense with ID %s not part of group with ID %s", expenseId, groupEntity.getGroupId());
                    log.error(msg);
                    return new InvalidExpenseDetailsException(msg);
                });
    }

    private void validateUpdateRequest(GroupExpenseEntity expenseEntity, ExpenseUpdateRequest updateRequest) throws InvalidExpenseDetailsException {
        boolean isUpdateRequired = false;
        Double amount = updateRequest.getAmount();
        String updateDescription = updateRequest.getDescription();
        String currentDescription = expenseEntity.getDescription();
        boolean isAmountEqual = amount != null && amount.equals(expenseEntity.getAmount());
        boolean isDescriptionEqual = StringUtils.hasText(updateDescription) &&
                updateDescription.equalsIgnoreCase(currentDescription);

        isUpdateRequired = (amount != null && !isAmountEqual) ||
                (StringUtils.hasText(updateDescription) && !isDescriptionEqual);
        //if entity and update request values are same
        if (!isUpdateRequired) {
            String msg = "found nothing to update for expenseId " + expenseEntity.getExpenseId();
            log.error(msg);
            throw new InvalidExpenseDetailsException(msg);
        }
        validateUpdatePayload(updateRequest, expenseEntity);
    }

    private void validateUpdatePayload(ExpenseUpdateRequest updateRequest, GroupExpenseEntity expenseEntity) throws InvalidExpenseDetailsException {
        Double amount = updateRequest.getAmount();
        if (amount != null && amount < 0) {
            String exceptionMsg = "amount must be greater than zero for expense id: " + expenseEntity.getExpenseId();
            log.error(exceptionMsg);
            throw new InvalidExpenseDetailsException(exceptionMsg);
        }
    }
}
