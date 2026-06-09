package org.example.fintect.encryptdecrypt.service;

import lombok.RequiredArgsConstructor;
import org.example.fintect.encryptdecrypt.repository.UserEDRepository;
import org.example.fintect.encryptdecrypt.eesponsemodel.UserResponse;
import org.example.fintect.encryptdecrypt.encryptionutils.AesEncryptionUtil;
import org.example.fintect.encryptdecrypt.entity.UserED;
import org.example.fintect.encryptdecrypt.requestemodel.UserRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEDService {

    private final UserEDRepository repository;
    private final AesEncryptionUtil aes;

    // Save User
    public UserResponse save(UserRequest request) throws Exception {

        UserED user = new UserED();

        user.setUsername(request.getUsername());

        // ENCRYPT BEFORE SAVE
        String encEmail = aes.encrypt(request.getEmail());
        String encPhone = aes.encrypt(request.getPhoneNumber());

        user.setEmail(encEmail);
        user.setPhoneNumber(encPhone);

        UserED saved = repository.save(user);

        // RETURN ENCRYPTED DATA (NO DECRYPT)
        return new UserResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),        // encrypted
                saved.getPhoneNumber()   // encrypted
        );
    }


    // Get User
    public UserResponse get(Long id) throws Exception {

        UserED user = repository.findById(id).orElseThrow();

        // Decrypt after read
        String email = aes.decrypt(user.getEmail());

        String phone = aes.decrypt(user.getPhoneNumber());

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                email,
                phone
        );
    }
}
