package org.example.fintect.customer;

import org.example.fintect.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;



@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    Customer findByEmail(String email);

    Customer findByNationalId(String nationalId);

    Customer findByPhone(String phone);

    Customer findByUserId(Long id);

    Customer findByUser(User adminUser);
}
