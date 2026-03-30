package org.example.fintect.statement;

import lombok.RequiredArgsConstructor;
import org.example.fintect.statement.model.StatementResponseModel;
import org.example.fintect.statement.specification.StatementSpecification;
import org.example.fintect.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatementService {

    private final StatementRepository statementRepository;

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

}
