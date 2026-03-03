package org.example.fintect.account.model;

import jakarta.persistence.*;
import lombok.Data;
import org.example.fintect.account.enumType.AccountStatus;
import org.example.fintect.account.enumType.AccountType;
import org.example.fintect.customer.Customer;
import org.example.fintect.customer.Status;
import org.example.fintect.user.Role;
import org.example.fintect.user.User;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AccountResponseModel {

    private Long id;
    private String fullName;
    private String nationalId;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private Status customeStatus;
    private String accountNo;
    private Long customerId;
    private AccountType accountType;
    private BigDecimal balance;
    private AccountStatus accountStatus;
    private LocalDateTime createdAt;
    private Role accountRole;


}
