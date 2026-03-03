package org.example.fintect.account;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.fintect.account.enumType.AccountStatus;
import org.example.fintect.account.enumType.AccountType;
import org.example.fintect.customer.Customer;
import org.example.fintect.customer.CustomerRepository;
import org.example.fintect.customer.Status;
import org.example.fintect.user.User;
import org.example.fintect.user.UsersRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SystemAccountInitializer {

    private final UsersRepository userRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    @PostConstruct
    public void createAdminAccountIfNotExists() {

        Optional<User> optionalUser =
                userRepository.findByEmail("anik+100@newroztech.com");

        if (optionalUser.isEmpty()) {
            System.out.println("❌ Admin user not found");
            return;
        }

        User adminUser = optionalUser.get();
        System.out.println("✅ Admin found");

        Customer adminCustomer =
                customerRepository.findByUser(adminUser);

        // 🔥 CREATE CUSTOMER IF NOT EXISTS
        if (adminCustomer == null) {

            adminCustomer = new Customer();
            adminCustomer.setFullName("SYSTEM ADMIN");
            adminCustomer.setEmail(adminUser.getEmail());
            adminCustomer.setPhone(adminUser.getContactNumber());
            adminCustomer.setUser(adminUser);
            adminCustomer.setStatus(Status.ACTIVE);

            customerRepository.save(adminCustomer);

            System.out.println("🔥 Admin customer profile created");
        }

        boolean accountExists =
                accountRepository.existsByCustomer(adminCustomer);

        if (accountExists) {
            System.out.println("⚠ Account already exists");
            return;
        }

        Account account = new Account();
        account.setAccountNo(generateAccountNo());
        account.setCustomer(adminCustomer);
        account.setAccountType(AccountType.CORPORATE);
        account.setBalance(new BigDecimal("1000000"));
        account.setStatus(AccountStatus.ACTIVE);

        accountRepository.save(account);

        System.out.println("🔥 Admin system account created");
    }

    private String generateAccountNo() {
        return "SYS" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }
}
