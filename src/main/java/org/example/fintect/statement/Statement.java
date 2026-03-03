package org.example.fintect.statement;

import jakarta.persistence.*;
import lombok.Data;
import org.example.fintect.account.Account;
import org.example.fintect.transaction.Transaction;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
    public class Statement {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        // Account owner of this statement row
        @ManyToOne
        @JoinColumn(name = "account_no", referencedColumnName = "accountNo")
        private Account account;

        // Linked transaction
        @ManyToOne
        @JoinColumn(name = "transaction_id")
        private Transaction transaction;

        @Column(nullable = false)
        private BigDecimal debit;

        @Column(nullable = false)
        private BigDecimal credit;

        private BigDecimal amount;

        private BigDecimal afterBalance;

        private String description;

        private String txId;

        @CreationTimestamp
        private LocalDateTime createdAt;

}
