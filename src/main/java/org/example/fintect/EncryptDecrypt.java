package org.example.fintect;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptDecrypt {



    public static void main(String[] args) throws Exception {
        SecretKey key = generateKey();
        String original = "Sensitive Data to Encrypt";
        String encrypted = encrypt(original, key);
        String decrypted = decrypt(encrypted, key);

        System.out.println("Original:  " + original);
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypted);
    }


    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // 128 or 256
        return keyGen.generateKey();
    }

    // Encrypt
    public static String encrypt(String plaintext, SecretKey key) throws Exception {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);  // Random IV every time
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        byte[] encrypted = cipher.doFinal(plaintext.getBytes("UTF-8"));

        // Prepend IV to ciphertext so we can use it during decryption
        byte[] ivAndCipher = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, ivAndCipher, 0, iv.length);
        System.arraycopy(encrypted, 0, ivAndCipher, iv.length, encrypted.length);

        return Base64.getEncoder().encodeToString(ivAndCipher);
    }

    // Decrypt
    public static String decrypt(String ciphertext, SecretKey key) throws Exception {
        byte[] ivAndCipher = Base64.getDecoder().decode(ciphertext);

        // Extract IV (first 16 bytes)
        byte[] iv = new byte[16];
        byte[] encrypted = new byte[ivAndCipher.length - 16];
        System.arraycopy(ivAndCipher, 0, iv, 0, 16);
        System.arraycopy(ivAndCipher, 16, encrypted, 0, encrypted.length);

        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

        return new String(cipher.doFinal(encrypted), "UTF-8");
    }


}
