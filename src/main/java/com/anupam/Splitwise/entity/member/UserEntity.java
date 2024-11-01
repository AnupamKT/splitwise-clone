package com.anupam.Splitwise.entity.member;

import com.anupam.Splitwise.entity.group.GroupEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "REGISTERED_USERS")
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;
    @Column(name = "user_name")
    private String name;
    @Column(name = "user_email", unique = true)
    private String email;
    private String mobileNumber;
    @Column(name = "user_password")
    @JsonIgnore
    private String password;

    @ManyToMany(mappedBy = "userEntities", fetch = FetchType.LAZY)
    @JsonBackReference
    Set<GroupEntity> groupEntities = new HashSet<>();
}
