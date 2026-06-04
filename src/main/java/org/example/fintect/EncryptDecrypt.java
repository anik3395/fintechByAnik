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
        String original = "How are you?";
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

        // Step 1: Create a 16-byte Initialization Vector (IV)
        // IV ensures that encrypting the same text multiple times
        // produces different ciphertexts.
        byte[] iv = new byte[16];

        // Generate a random IV for every encryption operation
        new SecureRandom().nextBytes(iv);

        // Wrap the IV in an IvParameterSpec object
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        // Step 2: Create and configure the Cipher
        // AES      -> Encryption algorithm
        // CBC      -> Cipher Block Chaining mode
        // PKCS5Padding -> Handles data that doesn't fit perfectly into AES blocks
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        // Initialize cipher in ENCRYPT mode using the secret key and IV
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        // Step 3: Convert plaintext into encrypted bytes
        byte[] encrypted = cipher.doFinal(plaintext.getBytes("UTF-8"));

        // Step 4: Combine IV + Encrypted Data
        // During decryption we need the same IV,
        // so we store it together with the ciphertext.
        byte[] ivAndCipher = new byte[iv.length + encrypted.length];

        // Copy IV to the beginning
        System.arraycopy(iv, 0, ivAndCipher, 0, iv.length);

        // Copy encrypted bytes right after the IV
        System.arraycopy(encrypted, 0, ivAndCipher, iv.length, encrypted.length);

        // Step 5: Convert binary data to Base64 string
        // Makes it easy to store in a database or send over APIs
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
