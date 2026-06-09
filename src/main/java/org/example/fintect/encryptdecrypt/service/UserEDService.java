package org.example.fintect.encryptdecrypt.service;

import lombok.RequiredArgsConstructor;
import org.example.fintect.encryptdecrypt.entity.Address;
import org.example.fintect.encryptdecrypt.repository.UserEDRepository;
import org.example.fintect.encryptdecrypt.responsemodel.UserResponse;
import org.example.fintect.encryptdecrypt.encryptionutils.AesEncryptionUtil;
import org.example.fintect.encryptdecrypt.entity.UserED;
import org.example.fintect.encryptdecrypt.requestemodel.UserRequest;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserEDService {

    private final UserEDRepository repository;
    private final AesEncryptionUtil aes;
    private final ObjectMapper objectMapper;

    // Save User
    public UserResponse save(UserRequest request) throws Exception {

        UserED user = new UserED();

        user.setUsername(request.getUsername());

        // Encrypt Email & Phone
        String encEmail = aes.encrypt(request.getEmail());
        String encPhone = aes.encrypt(request.getPhoneNumber());

        user.setEmail(encEmail);
        user.setPhoneNumber(encPhone);

        // Convert List<Address> to JSON
        String addressJson =
                objectMapper.writeValueAsString(
                        request.getAddresses()
                );

        // Encrypt Address JSON
        String encAddress = aes.encrypt(addressJson);

        user.setAddresses(encAddress);

        UserED saved = repository.save(user);

        // Decrypt Address JSON
        String decryptedAddressJson = aes.decrypt(saved.getAddresses());

        // JSON -> List<Address>
        List<Address> addresses =
                objectMapper.readValue(
                        decryptedAddressJson,
                        new TypeReference<List<Address>>() {}
                );

        return new UserResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),       // encrypted
                saved.getPhoneNumber(), // encrypted
                addresses               // decrypted List<Address>
        );
    }


    // Get User
    public UserResponse get(Long id) throws Exception {

        UserED user = repository.findById(id).orElseThrow();

        // Decrypt Email
        String email = aes.decrypt(user.getEmail());

        // Decrypt Phone
        String phone = aes.decrypt(user.getPhoneNumber());

        // Decrypt Addresses JSON
        String decryptedAddressJson = aes.decrypt(user.getAddresses());

        // Convert JSON -> List<Address>
        List<Address> addresses = objectMapper.readValue(
                        decryptedAddressJson,
                        new TypeReference<List<Address>>() {}
        );

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                email,
                phone,
                addresses
        );
    }
}
