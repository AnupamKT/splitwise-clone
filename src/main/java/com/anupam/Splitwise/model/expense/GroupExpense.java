package com.anupam.Splitwise.model.expense;

import com.anupam.Splitwise.entity.group.GroupEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupExpense {

    private Double amount;
    private String paidBy;
    private String description;
    @JsonIgnore
    private GroupEntity group;

}
