package org.example.fintect.encryptdecrypt.requestemodel;

import lombok.Data;
import org.example.fintect.encryptdecrypt.entity.Address;

import java.util.List;

@Data
public class UserRequest {

    private String username;

    private String email;

    private String phoneNumber;

    private List<Address> addresses;
}
