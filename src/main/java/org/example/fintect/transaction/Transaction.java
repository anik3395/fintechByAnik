package org.example.fintect.transaction;

import jakarta.persistence.*;
import lombok.Data;
import org.example.fintect.account.Account;
import org.example.fintect.account.enumType.TransactionStatus;
import org.example.fintect.account.enumType.TransactionType;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Transaction {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "sender_account_no", referencedColumnName = "accountNo")
        private Account senderAccountNo;

        @ManyToOne
        @JoinColumn(name = "receiver_account_no", referencedColumnName = "accountNo")
        private Account receiverAccountNo;

        @Column(nullable = false)
        private BigDecimal amount;

        @Enumerated(EnumType.STRING)
        private TransactionType transactionType;

        @Enumerated(EnumType.STRING)
        private TransactionStatus transactionStatus;

        private String txId;

        private String remarks; // optional note

        @CreationTimestamp
        private LocalDateTime createdAt;

}
