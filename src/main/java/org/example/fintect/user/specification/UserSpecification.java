package org.example.fintect.user.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.fintect.customer.Customer;
import org.example.fintect.customer.Status;
import org.example.fintect.user.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> withFilters(
            String name,
            String email,
            String contactNumber

    ) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();


            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
