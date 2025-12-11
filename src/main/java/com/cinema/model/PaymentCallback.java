package com.cinema.model;

public interface PaymentCallback {
    void onPaymentSuccess();
    void onPaymentCancelled();
}
