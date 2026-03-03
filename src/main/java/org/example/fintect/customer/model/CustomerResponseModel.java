package org.example.fintect.customer.model;

import lombok.Data;
import org.example.fintect.customer.Status;
import org.example.fintect.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CustomerResponseModel {

    private Long id;
    private String fullName;
    private String nationalId;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private Long userId;
    private Status status;
    private LocalDateTime createdAt;


}
