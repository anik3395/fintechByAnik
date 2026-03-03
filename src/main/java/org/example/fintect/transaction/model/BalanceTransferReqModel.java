package org.example.fintect.transaction.model;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class BalanceTransferReqModel {
    private String senderAccountNo;
    private String receiverAccountNo;
    private BigDecimal amount;
    private String remarks;
}
