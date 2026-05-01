package org.example.fintect.transaction;

import lombok.RequiredArgsConstructor;
import org.example.fintect.response.ApiResponse;
import org.example.fintect.transaction.model.BalanceTransferReqModel;
import org.example.fintect.transaction.model.DepositRequestModel;
import org.example.fintect.transaction.utils.TransactionEndPointUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.example.fintect.account.enumType.TransactionStatus;
import org.example.fintect.account.enumType.TransactionType;

@Controller
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping(TransactionEndPointUtils.CREATE_ADMIN_TO_CUSTOMER_DEPOSIT)
    public ResponseEntity<ApiResponse> createAdminToCustomerDeposit(@RequestBody DepositRequestModel depositRequestModel) {
        return new ResponseEntity<>(transactionService.createAdminToCustomerDeposit(depositRequestModel), HttpStatus.OK);
    }

    @PostMapping(TransactionEndPointUtils.CREATE_BALANCE_TRANSFER)
    public ResponseEntity<ApiResponse> createBalanceTransfer(@RequestBody BalanceTransferReqModel  balanceTransferReqModel) {
        return new ResponseEntity<>(transactionService.createBalanceTransfer(balanceTransferReqModel), HttpStatus.OK);
    }

    @GetMapping(TransactionEndPointUtils.FETCH_ALL_TRANSACTIONS)
    public ResponseEntity<ApiResponse> fetchAllTransactions(
            @RequestParam(required = false) String senderAccountNo,
            @RequestParam(required = false) String receiverAccountNo,
            @RequestParam(required = false) BigDecimal amountMin,
            @RequestParam(required = false) BigDecimal amountMax,
            @RequestParam(required = false) TransactionType transactionType,
            @RequestParam(required = false) TransactionStatus transactionStatus,
            @RequestParam(required = false) String txId,
            @RequestParam(required = false) String remarks,
            @RequestParam(required = false) LocalDateTime createdAtFrom,
            @RequestParam(required = false) LocalDateTime createdAtTo,
            Pageable pageable) {
        return new ResponseEntity<>(transactionService.fetchAllTransactions(
                senderAccountNo, receiverAccountNo, amountMin, amountMax, transactionType, transactionStatus, txId, remarks, createdAtFrom, createdAtTo, pageable), HttpStatus.OK);
    }
}
