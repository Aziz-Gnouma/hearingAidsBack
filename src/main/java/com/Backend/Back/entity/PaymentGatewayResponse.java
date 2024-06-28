package com.Backend.Back.entity;

public class PaymentGatewayResponse {
    private boolean success;
    private String message;
    private String transactionId; // Unique identifier for the transaction

    public PaymentGatewayResponse() {
    }

    public PaymentGatewayResponse(boolean success, String message, String transactionId) {
        this.success = success;
        this.message = message;
        this.transactionId = transactionId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
