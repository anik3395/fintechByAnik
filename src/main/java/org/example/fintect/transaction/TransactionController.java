package org.example.fintect.transaction;

import lombok.RequiredArgsConstructor;
import org.example.fintect.response.ApiResponse;
import org.example.fintect.transaction.model.BalanceTransferReqModel;
import org.example.fintect.transaction.model.DepositRequestModel;
import org.example.fintect.transaction.utils.TransactionEndPointUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
}
