package org.example.fintect.account;

import lombok.RequiredArgsConstructor;
import org.example.fintect.account.enumType.AccountStatus;
import org.example.fintect.account.model.AccountReqModel;
import org.example.fintect.account.model.AccountResponseModel;
import org.example.fintect.account.specification.AccountSpecification;
import org.example.fintect.customer.Customer;
import org.example.fintect.customer.CustomerRepository;
import org.example.fintect.customer.Status;
import org.example.fintect.customer.model.CustomerResponseModel;
import org.example.fintect.exceptions.InvalidDataException;
import org.example.fintect.response.ApiResponse;
import org.example.fintect.security.SecurityConfig;
import org.example.fintect.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final SecurityConfig securityConfig;

    public ApiResponse createAccount(AccountReqModel accountReqModel) {


        Customer customer = customerRepository.findByEmail(accountReqModel.getEmail());
        if (customer == null) {
            throw new InvalidDataException("Customer with email " + accountReqModel.getEmail() + " does not exist");
        }

        if(!customer.getStatus().equals(Status.APPROVED)) {
            throw new InvalidDataException("Customer with email " + accountReqModel.getEmail() + " is not approved or others issue " +
                    "Please wait for approval" );
        }


        User currentUser = SecurityConfig.getCurrentUser();
        if(!currentUser.getEmail().equals(accountReqModel.getEmail())) {
            throw new InvalidDataException("Current user email does not match account email");
        }



        Account account = new Account();
        account.setAccountNo(generateAccountNo());
        account.setAccountType(accountReqModel.getAccountType());
        account.setBalance(BigDecimal.ZERO);
        account.setStatus(AccountStatus.PENDING);
        account.setCustomer(customer);
        accountRepository.save(account);

        return ApiResponse.success("Account Created Successfully",null);

    }

    private String generateAccountNo() {
        return "ACC" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }


    public ApiResponse fetchAccount(String fullName,
                                    String email,
                                    String phone,
                                    Integer page,
                                    Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Specification<Account> specification = AccountSpecification.withFilters(fullName,phone,email);
        Page<Account> accountPage = accountRepository.findAll(specification, pageable);

        List<AccountResponseModel> responseModelList = new LinkedList<>();
        for(Account account : accountPage.getContent()) {
            AccountResponseModel accountResponseModel = new AccountResponseModel();

            accountResponseModel.setId(account.getId());
            accountResponseModel.setAccountNo(account.getAccountNo());
            accountResponseModel.setBalance(account.getBalance());
            accountResponseModel.setCreatedAt(account.getCreatedAt());
            accountResponseModel.setAccountStatus(account.getStatus());
            accountResponseModel.setAccountType(account.getAccountType());
            accountResponseModel.setCustomerId(account.getCustomer().getId());
            accountResponseModel.setFullName(account.getCustomer().getFullName());
            accountResponseModel.setEmail(account.getCustomer().getEmail());
            accountResponseModel.setPhone(account.getCustomer().getPhone());
            accountResponseModel.setCustomeStatus(account.getCustomer().getStatus());
            accountResponseModel.setNationalId(account.getCustomer().getNationalId());
            accountResponseModel.setDateOfBirth(account.getCustomer().getDateOfBirth());
            accountResponseModel.setAccountRole(account.getCustomer().getUser().getRole());
            responseModelList.add(accountResponseModel);

        }

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("content", responseModelList);
        responseData.put("currentPage", accountPage.getNumber());
        responseData.put("totalItems", accountPage.getTotalElements());
        responseData.put("totalPages", accountPage.getTotalPages());
        responseData.put("pageSize", accountPage.getSize());

        return ApiResponse.success("Customer Account list fetched successfully", responseData);

    }
}
