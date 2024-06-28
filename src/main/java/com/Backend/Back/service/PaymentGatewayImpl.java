package com.Backend.Back.service;

import com.Backend.Back.entity.PaymentGateway;
import com.Backend.Back.entity.PaymentGatewayResponse;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class PaymentGatewayImpl implements PaymentGateway {

    public PaymentGatewayResponse charge(String cardNumber, String expirationDate, String cvc) {
        String transactionId = generateTransactionId();
        return new PaymentGatewayResponse(true, "Payment successful", transactionId);
    }

    private String generateTransactionId() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        StringBuilder transactionIdBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            transactionIdBuilder.append(characters.charAt(random.nextInt(characters.length())));
        }

        return transactionIdBuilder.toString();
    }

}
