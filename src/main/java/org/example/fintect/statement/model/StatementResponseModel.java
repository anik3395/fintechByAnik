package org.example.fintect.statement.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StatementResponseModel {

    private Long id;
    private String accountNo;
    private String customerFullName;
    private String customerEmail;
    private Long transactionId;
    private String txId;
    private BigDecimal debit;
    private BigDecimal credit;
    private BigDecimal amount;
    private BigDecimal afterBalance;
    private String description;
    private LocalDateTime createdAt;

}
