package com.Backend.Back.entity;

public interface PaymentGateway {
    PaymentGatewayResponse charge(String cardNumber, String expirationDate, String cvc);
}
