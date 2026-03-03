package org.example.fintect.account.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.fintect.account.Account;
import org.example.fintect.customer.Customer;
import org.example.fintect.customer.Status;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AccountSpecification {

    public static Specification<Account> withFilters(
            String fullName,
            String email,
            String phone

    ) {
        return (Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();


            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
