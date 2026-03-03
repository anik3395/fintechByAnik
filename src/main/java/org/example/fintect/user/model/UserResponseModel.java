package org.example.fintect.user.model;

import lombok.Data;
import org.example.fintect.user.Role;

@Data
public class UserResponseModel {
    private Long id;
    private String name;
    private String email;
    private String contactNumber;
    private Role role;
}
