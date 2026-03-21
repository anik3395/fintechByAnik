package org.example.fintect.transaction;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.fintect.account.Account;
import org.example.fintect.account.AccountRepository;
import org.example.fintect.account.enumType.AccountStatus;
import org.example.fintect.account.enumType.TransactionStatus;
import org.example.fintect.account.enumType.TransactionType;
import org.example.fintect.customer.Customer;
import org.example.fintect.customer.CustomerRepository;
import org.example.fintect.customer.Status;
import org.example.fintect.exceptions.InvalidDataException;
import org.example.fintect.response.ApiResponse;
import org.example.fintect.security.SecurityConfig;
import org.example.fintect.statement.Statement;
import org.example.fintect.statement.StatementRepository;
import org.example.fintect.transaction.model.BalanceTransferReqModel;
import org.example.fintect.transaction.model.DepositRequestModel;
import org.example.fintect.user.Role;
import org.example.fintect.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final StatementRepository statementRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public ApiResponse createAdminToCustomerDeposit(DepositRequestModel depositRequestModel) {

        Account adminAccount = accountRepository.findByAccountNo(depositRequestModel.getAdminAccountNo());
        if (adminAccount == null) {
            throw new InvalidDataException("Admin account not found");
        }

        if (!adminAccount.getStatus().equals(AccountStatus.ACTIVE)) {
            throw new InvalidDataException("Admin account is not active.Please activate it");
        }

        Account customerAccount = accountRepository.findByAccountNo(depositRequestModel.getCustomerAccountNo());
        if (customerAccount == null) {
            throw new InvalidDataException("Customer account not found");
        }

        if (!customerAccount.getStatus().equals(AccountStatus.ACTIVE)) {
            throw new InvalidDataException("Customer account is not active.Please activate it");
        }

        Customer customer = customerRepository.findByEmail(customerAccount.getCustomer().getEmail());
        if (!customer.getStatus().equals(Status.APPROVED)) {
            throw new InvalidDataException("Please Approve first this customer");
        }

        BigDecimal amount = depositRequestModel.getAmount();

        if (adminAccount.getBalance().compareTo(amount) < 0) {
            throw new InvalidDataException("Insufficient admin balance");
        }

        // ===============================
        // CREATE MAIN TRANSACTION (PENDING)
        // ===============================

        Transaction transaction = new Transaction();
        transaction.setSenderAccountNo(adminAccount);
        transaction.setReceiverAccountNo(customerAccount);
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transaction.setTxId(successGenerateTxId());
        transaction.setRemarks(depositRequestModel.getRemarks());
        transactionRepository.save(transaction);

        try {

            // ===============================
            // MOVE MONEY
            // ===============================

            adminAccount.setBalance(adminAccount.getBalance().subtract(amount));
            customerAccount.setBalance(customerAccount.getBalance().add(amount));

            accountRepository.save(adminAccount);
            accountRepository.save(customerAccount);

            // ===============================
            // MARK SUCCESS
            // ===============================

            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
            transactionRepository.save(transaction);

            // ===============================
            // SAVE SUCCESS STATEMENTS
            // ===============================

            Statement adminStatement = new Statement();
            adminStatement.setAccount(adminAccount);
            adminStatement.setTransaction(transaction);
            adminStatement.setDebit(amount);
            adminStatement.setCredit(BigDecimal.ZERO);
            adminStatement.setAfterBalance(adminAccount.getBalance());
            adminStatement.setTxId(transaction.getTxId());
            adminStatement.setAmount(amount);
            adminStatement.setDescription("Deposit to " + customerAccount.getAccountNo());
            statementRepository.save(adminStatement);

            Statement customerStatement = new Statement();
            customerStatement.setAccount(customerAccount);
            customerStatement.setTransaction(transaction);
            customerStatement.setDebit(BigDecimal.ZERO);
            customerStatement.setCredit(amount);
            customerStatement.setAfterBalance(customerAccount.getBalance());
            customerStatement.setTxId(transaction.getTxId());
            customerStatement.setAmount(amount);
            customerStatement.setDescription("Cash deposit from " + adminAccount.getAccountNo());
            statementRepository.save(customerStatement);

            // ===============================
            // SIMULATE FAILURE
            // ===============================

            if (new Random().nextBoolean()) {
                throw new RuntimeException("Simulated failure");
            }

            return ApiResponse.success("Deposit Success", null);

        } catch (Exception ex) {

            // ===============================
            // REVERSE MONEY
            // ===============================

            adminAccount.setBalance(adminAccount.getBalance().add(amount));
            customerAccount.setBalance(customerAccount.getBalance().subtract(amount));

            accountRepository.save(adminAccount);
            accountRepository.save(customerAccount);

            // ===============================
            // CREATE REVERSAL TRANSACTION
            // ===============================

            Transaction reversalTransaction = new Transaction();
            reversalTransaction.setSenderAccountNo(customerAccount);
            reversalTransaction.setReceiverAccountNo(adminAccount);
            reversalTransaction.setAmount(amount);
            reversalTransaction.setTransactionType(TransactionType.DEPOSIT);
            reversalTransaction.setTransactionStatus(TransactionStatus.FAILED);
            reversalTransaction.setTxId(successGenerateTxId());
            reversalTransaction.setRemarks("Reversal of TXID: " + transaction.getTxId());
            transactionRepository.save(reversalTransaction);

            // ===============================
            // SAVE REVERSAL STATEMENTS
            // ===============================

            Statement adminReversal = new Statement();
            adminReversal.setAccount(adminAccount);
            adminReversal.setTransaction(reversalTransaction);
            adminReversal.setDebit(BigDecimal.ZERO);
            adminReversal.setCredit(amount);
            adminReversal.setAfterBalance(adminAccount.getBalance());
            adminReversal.setTxId(reversalTransaction.getTxId());
            adminReversal.setAmount(amount);
            adminReversal.setDescription("REVERSAL - Deposit failed");
            statementRepository.save(adminReversal);

            Statement customerReversal = new Statement();
            customerReversal.setAccount(customerAccount);
            customerReversal.setTransaction(reversalTransaction);
            customerReversal.setDebit(amount);
            customerReversal.setCredit(BigDecimal.ZERO);
            customerReversal.setAfterBalance(customerAccount.getBalance());
            customerReversal.setTxId(reversalTransaction.getTxId());
            customerReversal.setAmount(amount);
            customerReversal.setDescription("REVERSAL - Deposit failed");
            statementRepository.save(customerReversal);

            return ApiResponse.error("Deposit failed and reversed");
        }
    }




    @Transactional
    public ApiResponse createBalanceTransfer(BalanceTransferReqModel balanceTransferReqModel) {

        User currentUser = SecurityConfig.getCurrentUser();
        System.err.println("Current user: " + currentUser);


        Account senderAccount = accountRepository.findByAccountNo(balanceTransferReqModel.getSenderAccountNo());
        if(senderAccount == null) {
            throw new InvalidDataException("Sender account not found");
        }

        if(!senderAccount.getCustomer().getUser().equals(currentUser)) {
            throw new InvalidDataException("This sender account is not current account");
        }

        if(!senderAccount.getStatus().equals(AccountStatus.ACTIVE)) {
            throw new InvalidDataException("Sender account is not active");
        }

        if(!senderAccount.getCustomer().getStatus().equals(Status.APPROVED)){
            throw new InvalidDataException("Sender account is not approved");
        }




        Account receiverAccount = accountRepository.findByAccountNo(balanceTransferReqModel.getReceiverAccountNo());
        if(receiverAccount == null) {
            throw new InvalidDataException("Receiver account not found");
        }
        if(!receiverAccount.getStatus().equals(AccountStatus.ACTIVE)) {
            throw new InvalidDataException("Receiver account is not active");
        }
        if(!receiverAccount.getCustomer().getStatus().equals(Status.APPROVED)){
            throw new InvalidDataException("Receiver account is not approved");
        }

        if(senderAccount.getBalance().compareTo(balanceTransferReqModel.getAmount()) < 0) {
            throw new InvalidDataException("Insufficient balance for sender account");
        }


        //Update Balance and save into DB
        senderAccount.setBalance(senderAccount.getBalance().subtract(balanceTransferReqModel.getAmount()));
        receiverAccount.setBalance(receiverAccount.getBalance().add(balanceTransferReqModel.getAmount()));

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        //Update Transaction
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setSenderAccountNo(senderAccount);
        transaction.setReceiverAccountNo(receiverAccount);
        transaction.setAmount(balanceTransferReqModel.getAmount());
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction.setTxId(successGenerateTxId());
        transaction.setRemarks(balanceTransferReqModel.getRemarks());
        transactionRepository.save(transaction);

        //Update statement for Credit
        Statement senderStatement = new Statement();
        senderStatement.setAccount(senderAccount);
        senderStatement.setTransaction(transaction);
        senderStatement.setDebit(balanceTransferReqModel.getAmount());
        senderStatement.setCredit(BigDecimal.ZERO);
        senderStatement.setAfterBalance(senderAccount.getBalance());
        senderStatement.setTxId(transaction.getTxId());
        senderStatement.setAmount(balanceTransferReqModel.getAmount());
        senderStatement.setDescription("SEND SUCCESSFULLY from " + senderAccount.getAccountNo());
        statementRepository.save(senderStatement);

        //Update statement for Debit
        Statement receiverStatement = new Statement();
        receiverStatement.setAccount(receiverAccount);
        receiverStatement.setTransaction(transaction);
        receiverStatement.setDebit(BigDecimal.ZERO);
        receiverStatement.setCredit(balanceTransferReqModel.getAmount());
        receiverStatement.setAfterBalance(receiverAccount.getBalance());
        receiverStatement.setTxId(transaction.getTxId());
        receiverStatement.setAmount(balanceTransferReqModel.getAmount());
        receiverStatement.setDescription("RECEIVED to " + receiverAccount.getAccountNo());
        statementRepository.save(receiverStatement);


        return ApiResponse.success("Transaction Successfully Done", null);


    }



    private String successGenerateTxId() {
        return "TXID" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 10)
                .toUpperCase();
    }

    public ApiResponse fetchAllTransactions() {

            List<Transaction> transactionList = transactionRepository.findAll();

            if (transactionList.isEmpty()) {
               throw new InvalidDataException("No transactions found");
            }

            return new ApiResponse(
                    true,
                    "All transactions fetched successfully",
                    transactionList
            );

    }
}
