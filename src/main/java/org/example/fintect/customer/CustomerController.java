package org.example.fintect.customer;

import lombok.RequiredArgsConstructor;
import org.example.fintect.customer.model.CustomerReqModel;
import org.example.fintect.customer.utils.CustomerEndPointUtils;
import org.example.fintect.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping(CustomerEndPointUtils.SAVE_CUSTOMER)
    public ResponseEntity saveCustomer(@RequestBody CustomerReqModel customerReqModel) {
        Customer response = customerService.saveCustomer(customerReqModel);
        return ResponseEntity.ok(response);
    }


    @GetMapping(CustomerEndPointUtils.GET_CUSTOMER)
    public ResponseEntity<ApiResponse> getCustomer(@RequestParam (required = false) String fullName,
                                                   @RequestParam (required = false) String email,
                                                   @RequestParam (required = false) String phone,
                                                   @RequestParam (required = false) Status status,
                                                   @RequestParam(required = false, defaultValue = "0") Integer page,
                                                   @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return new  ResponseEntity<>(customerService.fetchCustomer(fullName,email,phone,status,page,size), HttpStatus.OK);
    }


    @PutMapping(CustomerEndPointUtils.UPDATE_CUSTOMER_STATUS)
    public ResponseEntity<ApiResponse> updateCustomerStatus(@RequestParam String email,
                                                            @RequestParam Status status) {
        return new ResponseEntity<>(customerService.updateCustomerStatus(email,status),HttpStatus.OK);
    }




}
