package com.anupam.Splitwise.entity.group;

import com.anupam.Splitwise.entity.expense.GroupExpenseEntity;
import com.anupam.Splitwise.entity.expense.SettlementsEntity;
import com.anupam.Splitwise.entity.member.UserEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "GRP_DETAILS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID groupId;
    private String groupName;
    private String groupAdmin;
    private Date createdDate;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    @JoinTable(name = "group_members",
            joinColumns = @JoinColumn(name = "groupId"),
            inverseJoinColumns = @JoinColumn(name = "userId"))
    @JsonManagedReference
    private Set<UserEntity> userEntities = new HashSet<>();
    @OneToMany(mappedBy = "group",fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<GroupExpenseEntity>expenses=new HashSet<>();
    @OneToMany(mappedBy = "group",fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<SettlementsEntity> settlements= new HashSet<>();
}
