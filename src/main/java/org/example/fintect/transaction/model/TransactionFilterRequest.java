package org.example.fintect.transaction.model;

import lombok.Data;
import org.example.fintect.account.enumType.TransactionStatus;
import org.example.fintect.account.enumType.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionFilterRequest {
    private String senderAccountNo;
    private String receiverAccountNo;
    private BigDecimal amountMin;
    private BigDecimal amountMax;
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;
    private String txId;
    private String remarks;
    private LocalDateTime createdAtFrom;
    private LocalDateTime createdAtTo;
}
