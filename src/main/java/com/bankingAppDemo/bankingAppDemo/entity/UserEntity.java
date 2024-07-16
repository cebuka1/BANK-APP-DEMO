package com.bankingAppDemo.bankingAppDemo.entity;

import com.bankingAppDemo.bankingAppDemo.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "users_tbl")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity extends BaseClass{
    private String firstName;

    private String lastName;

    private String otherName;

    private String email;

    private String password;

    private String gender;

    private String address;

    private String stateOfOrigin;

    private String phoneNumber;

    private String BVN;

    private String pin;

    private String accountNumber;

    private BigDecimal accountBalance;

    private String bankName;

    private String profilePicture;

    private String status;

    private Role role;
}