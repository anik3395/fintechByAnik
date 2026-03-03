package org.example.fintect.account;

import jakarta.persistence.*;
import lombok.Data;
import org.example.fintect.account.enumType.AccountStatus;
import org.example.fintect.account.enumType.AccountType;
import org.example.fintect.customer.Customer;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Account {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true, nullable = false)
        private String accountNo;

        @ManyToOne
        @JoinColumn(name = "customer_id", nullable = false)
        private Customer customer;

        @Enumerated(EnumType.STRING)
        private AccountType accountType; // SAVINGS, CURRENT

        private BigDecimal balance;

        @Enumerated(EnumType.STRING)
        private AccountStatus status; // ACTIVE, BLOCKED, CLOSED

        @CreationTimestamp
        private LocalDateTime createdAt;


    }




