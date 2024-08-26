package com.anupam.Splitwise.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "uniqueotp",columnNames = {"mobileNumber","otp"})})
public class OneTimePassword {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String mobileNumber;
    private long otp;
    private boolean isExpired;
    private LocalDateTime createDate;

}
