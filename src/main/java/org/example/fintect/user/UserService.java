package org.example.fintect.user;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.LifecycleState;
import org.example.fintect.customer.Customer;
import org.example.fintect.customer.CustomerRepository;
import org.example.fintect.response.ApiResponse;
import org.example.fintect.user.model.UserResponseModel;
import org.example.fintect.user.specification.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UsersRepository usersRepository;
    private final CustomerRepository customerRepository;

    public ApiResponse fetchAllUsers(String name,
                                     String email,
                                     String contactNumber,
                                     Integer page,
                                     Integer size) {

         Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "name"));
         Specification<User> spec = UserSpecification.withFilters(name,email,contactNumber);

         Page<User> userPageable = usersRepository.findAll(spec, pageable);

        List<UserResponseModel> responseModelList = new ArrayList<>();
        for (User user : userPageable.getContent()) {
            UserResponseModel responseModel = new UserResponseModel();
            responseModel.setId(user.getId());
            responseModel.setEmail(user.getEmail());
            responseModel.setName(user.getName());
            responseModel.setContactNumber(user.getContactNumber());
            responseModel.setRole(user.getRole());
            responseModelList.add(responseModel);

            if (user.getRole().equals(Role.CUSTOMER)) {

                Customer customer = customerRepository.findByUserId(user.getId());

                if (customer != null) {
                    responseModel.setName(customer.getFullName());
                    responseModel.setContactNumber(customer.getPhone());
                }else {
                    responseModel.setName(null);
                    responseModel.setContactNumber(null);
                }
            }
        }
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("content", responseModelList);
        responseData.put("currentPage", userPageable.getNumber());
        responseData.put("totalItems", userPageable.getTotalElements());
        responseData.put("totalPages", userPageable.getTotalPages());
        responseData.put("pageSize", userPageable.getSize());

        return ApiResponse.success("Usee list fetched successfully", responseData);
    }
}
