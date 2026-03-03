package org.example.fintect.customer.model;

import lombok.Data;

import java.time.LocalDate;
@Data
public class CustomerReqModel {
    private String fullName;
    private String email;
    private String phone;
    private String nationalId;
    private LocalDate dateOfBirth;
    private Long userId;
}
