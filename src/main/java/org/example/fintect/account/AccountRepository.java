package org.example.fintect.account;

import org.example.fintect.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository  extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
    boolean existsByCustomer(Customer adminCustomer);

    Account findByAccountNo(String adminAccountNo);

}
