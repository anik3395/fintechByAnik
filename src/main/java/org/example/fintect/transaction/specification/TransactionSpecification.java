package org.example.fintect.transaction.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.fintect.account.enumType.TransactionStatus;
import org.example.fintect.account.enumType.TransactionType;
import org.example.fintect.transaction.Transaction;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionSpecification {

    public static Specification<Transaction> withFilters(
            String senderAccountNo,
            String receiverAccountNo,
            BigDecimal amountMin,
            BigDecimal amountMax,
            TransactionType transactionType,
            TransactionStatus transactionStatus,
            String txId,
            String remarks,
            LocalDateTime createdAtFrom,
            LocalDateTime createdAtTo
    ) {
        return (Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (senderAccountNo != null && !senderAccountNo.isEmpty()) {
                predicates.add(cb.equal(root.get("senderAccountNo").get("accountNo"), senderAccountNo));
            }

            if (receiverAccountNo != null && !receiverAccountNo.isEmpty()) {
                predicates.add(cb.equal(root.get("receiverAccountNo").get("accountNo"), receiverAccountNo));
            }

            if (amountMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("amount"), amountMin));
            }

            if (amountMax != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("amount"), amountMax));
            }

            if (transactionType != null) {
                predicates.add(cb.equal(root.get("transactionType"), transactionType));
            }

            if (transactionStatus != null) {
                predicates.add(cb.equal(root.get("transactionStatus"), transactionStatus));
            }

            if (txId != null && !txId.isEmpty()) {
                predicates.add(cb.like(root.get("txId"), "%" + txId + "%"));
            }

            if (remarks != null && !remarks.isEmpty()) {
                predicates.add(cb.like(root.get("remarks"), "%" + remarks + "%"));
            }

            if (createdAtFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), createdAtFrom));
            }

            if (createdAtTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), createdAtTo));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
