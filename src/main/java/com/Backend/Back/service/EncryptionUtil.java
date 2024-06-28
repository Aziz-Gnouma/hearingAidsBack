package com.Backend.Back.service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionUtil {

    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 256; // AES-256
    private static final SecretKey SECRET_KEY = generateSecretKey();

    private static SecretKey generateSecretKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(KEY_SIZE, new SecureRandom());
            return keyGen.generateKey();
        } catch (Exception e) {
            throw new RuntimeException("Error generating secret key", e);
        }
    }

    public static String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }

    public static String decrypt(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data", e);
        }
    }

    public static String maskAndEncryptCreditCard(String cardNumber) {
        if (cardNumber.length() <= 4) {
            return cardNumber; // If the card number is too short, return it as is.
        }

        String first14Digits = cardNumber.substring(0, 14);
        String last4Digits = cardNumber.substring(cardNumber.length() - 4);

        String encryptedFirst14Digits = encrypt(first14Digits);
        String maskedEncryptedFirst14Digits = maskEncryptedCardNumber(encryptedFirst14Digits);

        return maskedEncryptedFirst14Digits + last4Digits;
    }

    private static String maskEncryptedCardNumber(String encryptedCardNumber) {
        // Replace all but the last 4 characters with asterisks
        int length = encryptedCardNumber.length();
        if (length <= 4) {
            return encryptedCardNumber; // If the encrypted string is too short, return it as is.
        }
        StringBuilder masked = new StringBuilder();
        for (int i = 0; i < length - 4; i++) {
            masked.append('*');
        }
        masked.append(encryptedCardNumber.substring(length - 4));
        return masked.toString();
    }

    public static String maskCreditCard(String cardNumber) {
        int length = cardNumber.length();
        if (length <= 4) {
            return cardNumber; // If the card number is too short, return it as is.
        }
        StringBuilder masked = new StringBuilder();
        for (int i = 0; i < length - 4; i++) {
            masked.append('*');
        }
        masked.append(cardNumber.substring(length - 4));
        return masked.toString();
    }
}
