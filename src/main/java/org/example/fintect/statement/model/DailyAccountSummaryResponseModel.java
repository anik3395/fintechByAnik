package org.example.fintect.statement.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DailyAccountSummaryResponseModel {
    private LocalDate date;
    private String accountNo;
    private String accountHolderName;
    private String email;
    private String phoneNumber;
    private BigDecimal totalDebit;
    private BigDecimal totalCredit;
    private BigDecimal netBalance;
    private Long transactionCount;
}
