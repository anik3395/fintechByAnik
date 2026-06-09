package org.example.fintect.encryptdecrypt.encryptionutils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class AesEncryptionUtil {

    private final SecretKey key;

    public String encrypt(String plaintext) throws Exception {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        byte[] encrypted = cipher.doFinal(plaintext.getBytes("UTF-8"));

        byte[] ivAndCipher = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, ivAndCipher, 0, iv.length);
        System.arraycopy(encrypted, 0, ivAndCipher, iv.length, encrypted.length);

        return Base64.getEncoder().encodeToString(ivAndCipher);
    }

    public String decrypt(String ciphertext) throws Exception {
        byte[] ivAndCipher = Base64.getDecoder().decode(ciphertext);

        byte[] iv        = new byte[16];
        byte[] encrypted = new byte[ivAndCipher.length - 16];
        System.arraycopy(ivAndCipher, 0, iv, 0, 16);
        System.arraycopy(ivAndCipher, 16, encrypted, 0, encrypted.length);

        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

        return new String(cipher.doFinal(encrypted), "UTF-8");
    }
}
