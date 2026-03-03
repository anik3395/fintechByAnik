package org.example.fintect.transaction.model;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class DepositRequestModel {
    private String adminAccountNo;
    private String customerAccountNo;
    private BigDecimal amount;
    private String remarks;
}
