package org.example.fintect.customer;

import lombok.RequiredArgsConstructor;
import org.example.fintect.customer.model.CustomerReqModel;
import org.example.fintect.customer.model.CustomerResponseModel;
import org.example.fintect.customer.specification.CustomerSpecification;
import org.example.fintect.exceptions.InvalidDataException;
import org.example.fintect.response.ApiResponse;
import org.example.fintect.user.Role;
import org.example.fintect.user.User;
import org.example.fintect.user.UsersRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final UsersRepository  usersRepository;

    public Customer saveCustomer(CustomerReqModel customerReqModel) {
        Customer existingEmail = customerRepository.findByEmail(customerReqModel.getEmail());
        if (existingEmail != null) {
            throw new InvalidDataException("Customer already exists against this email");
        }


        Customer existingNationalId = customerRepository.findByNationalId(customerReqModel.getNationalId());
        if (existingNationalId != null) {
            throw new InvalidDataException("Customer already exists against this National Id");
        }

        Customer existingPhone = customerRepository.findByPhone(customerReqModel.getPhone());
        if (existingPhone != null) {
            throw new InvalidDataException("Customer already exists against this Phone Number");
        }

        User existingUserByUserId = usersRepository.findById(customerReqModel.getUserId()).orElse(null);
        if(!existingUserByUserId.getEmail().equals(customerReqModel.getEmail())) {
            throw new InvalidDataException("This is not proper userId against this email or customer");
        }

        User existingEmails = usersRepository.findByEmail(customerReqModel.getEmail()).orElse(null);
        if(existingEmails == null) {
            throw new InvalidDataException("This email is not exists in users list");
        }

        Customer newCustomer = new Customer();
        newCustomer.setEmail(customerReqModel.getEmail());
        newCustomer.setNationalId(customerReqModel.getNationalId());
        newCustomer.setPhone(customerReqModel.getPhone());
        newCustomer.setFullName(customerReqModel.getFullName());
        newCustomer.setDateOfBirth(customerReqModel.getDateOfBirth());
        newCustomer.setStatus(Status.PENDING);
        newCustomer.setUser(existingUserByUserId);
        return customerRepository.save(newCustomer);



    }

    public ApiResponse fetchCustomer(String fullName,
                                     String email,
                                     String phone,
                                     Status status,
                                     Integer page,
                                     Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Specification<Customer> specification = CustomerSpecification.withFilters(
                fullName,
                email,
                phone,
                status
        );

        Page<Customer> customerPage = customerRepository.findAll(specification,pageable);

        List<CustomerResponseModel> responseModelList = new LinkedList<>();
        for (Customer customer : customerPage.getContent()) {
            CustomerResponseModel responseModel = new CustomerResponseModel();
            responseModel.setId(customer.getId());
            responseModel.setFullName(customer.getFullName());
            responseModel.setEmail(customer.getEmail());
            responseModel.setPhone(customer.getPhone());
            responseModel.setNationalId(customer.getNationalId());
            responseModel.setDateOfBirth(customer.getDateOfBirth());
            responseModel.setStatus(customer.getStatus());
            responseModel.setCreatedAt(customer.getCreatedAt());
            responseModel.setUserId(customer.getUser().getId());
            responseModelList.add(responseModel);

        }

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("content", responseModelList);
        responseData.put("currentPage", customerPage.getNumber());
        responseData.put("totalItems", customerPage.getTotalElements());
        responseData.put("totalPages", customerPage.getTotalPages());
        responseData.put("pageSize", customerPage.getSize());

        return ApiResponse.success("Customer list fetched successfully", responseData);

    }

    public ApiResponse updateCustomerStatus(String email, Status status) {


        User existingEmail = usersRepository.findByEmail(email).orElse(null);
        if(existingEmail == null) {
            throw new InvalidDataException("This email is not exists in users list");
        }

        if(!existingEmail.getRole().equals(Role.CUSTOMER)){
            throw new InvalidDataException("Only customer status can be updated");
        }

        Customer customer = customerRepository.findByEmail(email);
        if(customer == null) {
            throw new InvalidDataException("This email is not exists in customer list");
        }

        customer.setStatus(status);
        customerRepository.save(customer);
        return ApiResponse.success("Customer status updated successfully", HttpStatus.OK);
    }



    @Cacheable(value = "customer", key = "#email")
    public ApiResponse fetchCustomerById(String email) {


        Customer customer = customerRepository.findByEmail(email);
        if(customer == null){
            throw new InvalidDataException("Customer not found");
        }
        return ApiResponse.builder()
                .success(true)
                .message("Customer fetched successfully")
                .data(customer)
                .build();

    }
}
