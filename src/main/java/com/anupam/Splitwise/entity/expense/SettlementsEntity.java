package com.anupam.Splitwise.entity.expense;

import com.anupam.Splitwise.entity.group.GroupEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "SETTLEMENT_DETAILS")
public class SettlementsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID settlementId;
    private String settlementAction;
    @ManyToOne
    @JoinColumn(name = "groupId")
    @JsonBackReference
    private GroupEntity group;
    private Date createdDate;
}
