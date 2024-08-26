package com.anupam.Splitwise.repository.expense;

import com.anupam.Splitwise.entity.expense.GroupExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<GroupExpenseEntity, UUID> {
}
