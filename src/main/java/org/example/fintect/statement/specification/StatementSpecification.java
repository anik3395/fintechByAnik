package org.example.fintect.statement.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.fintect.statement.Statement;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StatementSpecification {

    public static Specification<Statement> withFilters(
            String accountNo,
            String customerFullName,
            String customerEmail,
            String txId,
            LocalDateTime fromDate,
            LocalDateTime toDate
    ) {
        return (Root<Statement> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (accountNo != null && !accountNo.isEmpty()) {
                predicates.add(cb.equal(root.get("account").get("accountNo"), accountNo));
            }

            if (customerFullName != null && !customerFullName.isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("account").get("customer").get("fullName")), "%" + customerFullName.toLowerCase() + "%"));
            }

            if (customerEmail != null && !customerEmail.isEmpty()) {
                predicates.add(cb.equal(root.get("account").get("customer").get("email"), customerEmail));
            }

            if (txId != null && !txId.isEmpty()) {
                predicates.add(cb.equal(root.get("txId"), txId));
            }

            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate));
            }

            if (toDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), toDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
