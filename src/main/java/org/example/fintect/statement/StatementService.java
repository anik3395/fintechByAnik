package org.example.fintect.statement;

import lombok.RequiredArgsConstructor;
import org.example.fintect.account.Account;
import org.example.fintect.account.AccountRepository;
import org.example.fintect.statement.model.DailyAccountSummaryResponseModel;
import org.example.fintect.statement.model.StatementResponseModel;
import org.example.fintect.statement.specification.StatementSpecification;
import org.example.fintect.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatementService {

    private final StatementRepository statementRepository;
    private final AccountRepository accountRepository;

    public ApiResponse fetchStatements(String accountNo,
                                       String customerFullName,
                                       String customerEmail,
                                       String txId,
                                       LocalDateTime fromDate,
                                       LocalDateTime toDate,
                                       Integer page,
                                       Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Specification<Statement> specification = StatementSpecification.withFilters(
                accountNo,
                customerFullName,
                customerEmail,
                txId,
                fromDate,
                toDate
        );

        Page<Statement> statementPage = statementRepository.findAll(specification, pageable);

        List<StatementResponseModel> responseModelList = new LinkedList<>();
        for (Statement statement : statementPage.getContent()) {
            StatementResponseModel responseModel = new StatementResponseModel();
            responseModel.setId(statement.getId());
            responseModel.setAccountNo(statement.getAccount().getAccountNo());
            responseModel.setCustomerFullName(statement.getAccount().getCustomer().getFullName());
            responseModel.setCustomerEmail(statement.getAccount().getCustomer().getEmail());
            responseModel.setTransactionId(statement.getTransaction() != null ? statement.getTransaction().getId() : null);
            responseModel.setTxId(statement.getTxId());
            responseModel.setDebit(statement.getDebit());
            responseModel.setCredit(statement.getCredit());
            responseModel.setAmount(statement.getAmount());
            responseModel.setAfterBalance(statement.getAfterBalance());
            responseModel.setDescription(statement.getDescription());
            responseModel.setCreatedAt(statement.getCreatedAt());
            responseModelList.add(responseModel);
        }

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("content", responseModelList);
        responseData.put("currentPage", statementPage.getNumber());
        responseData.put("totalItems", statementPage.getTotalElements());
        responseData.put("totalPages", statementPage.getTotalPages());
        responseData.put("pageSize", statementPage.getSize());

        return ApiResponse.success("Statement list fetched successfully", responseData);
    }

    public ApiResponse getAllAccountsDailySummary() {
        // Fetch all statements
        List<Statement> statements = statementRepository.findAll(Sort.by(Sort.Direction.ASC, "createdAt"));

        // Group by date and account
        Map<LocalDate, Map<String, List<Statement>>> groupedByDateAndAccount = statements.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getCreatedAt().toLocalDate(),
                        TreeMap::new,
                        Collectors.groupingBy(s -> s.getAccount().getAccountNo())
                ));

        // Build response - flat list
        List<DailyAccountSummaryResponseModel> allSummaries = new LinkedList<>();

        for (Map.Entry<LocalDate, Map<String, List<Statement>>> dateEntry : groupedByDateAndAccount.entrySet()) {
            LocalDate date = dateEntry.getKey();

            for (Map.Entry<String, List<Statement>> accountEntry : dateEntry.getValue().entrySet()) {
                String accountNo = accountEntry.getKey();
                List<Statement> accountStatements = accountEntry.getValue();

                // Calculate totals
                BigDecimal totalDebit = accountStatements.stream()
                        .map(Statement::getDebit)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalCredit = accountStatements.stream()
                        .map(Statement::getCredit)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal netBalance = totalCredit.subtract(totalDebit);
                Long transactionCount = (long) accountStatements.size();

                // Get account details
                Account account = accountRepository.findByAccountNo(accountNo);
                if (account != null) {
                    DailyAccountSummaryResponseModel summary = new DailyAccountSummaryResponseModel();
                    summary.setDate(date);
                    summary.setAccountNo(accountNo);
                    summary.setAccountHolderName(account.getCustomer().getFullName());
                    summary.setEmail(account.getCustomer().getEmail());
                    summary.setPhoneNumber(account.getCustomer().getPhone());
                    summary.setTotalDebit(totalDebit);
                    summary.setTotalCredit(totalCredit);
                    summary.setNetBalance(netBalance);
                    summary.setTransactionCount(transactionCount);

                    allSummaries.add(summary);
                }
            }
        }

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("totalRecords", allSummaries.size());
        responseData.put("dailySummaries", allSummaries);

        return ApiResponse.success("Daily account summaries fetched successfully", responseData);
    }

}
