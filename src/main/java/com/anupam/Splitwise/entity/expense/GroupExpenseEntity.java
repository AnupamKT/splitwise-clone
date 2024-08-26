package com.anupam.Splitwise.entity.expense;

import com.anupam.Splitwise.entity.group.GroupEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "EXPENSE_DETAILS")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID expenseId;
    private Integer amount;
    private String paidBy;
    private String description;
    @ManyToOne
    @JoinColumn(name = "groupId")
    @JsonBackReference
    private GroupEntity group;
    private Date createdDate;
}
