package com.anupam.Splitwise.entity.audit;

import com.anupam.Splitwise.entity.group.GroupEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "audit_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID activityId;
    @ManyToOne
    @JoinColumn(name = "groupId")
    @JsonBackReference
    private GroupEntity group;
    private String activityName;
    private Date createdDate;
}
