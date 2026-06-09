package org.example.fintect.encryptdecrypt.responsemodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.fintect.encryptdecrypt.entity.Address;

import java.util.List;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long id;

    private String username;

    private String email;

    private String phoneNumber;


    private List<Address> addresses;
}
