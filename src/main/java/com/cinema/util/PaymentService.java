package com.cinema.util;

import com.cinema.model.Payment;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaymentService {
    private static final double TICKET_PRICE = 250;

    public static double getTicketPrice() {
        return TICKET_PRICE;
    }

    public static boolean processPayment(String movieTitle, String showTime, String seatNumber,
                                         double amountPaid, String customerName) {
        if (amountPaid < TICKET_PRICE) {
            return false;
        }

        String paymentId = "PAY" + System.currentTimeMillis();

        Payment payment = new Payment(paymentId, movieTitle, showTime, seatNumber,
                TICKET_PRICE, amountPaid, customerName);

        return saveReceipt(payment);
    }

    private static boolean saveReceipt(Payment payment) {
        try {
            String fileName = String.format("src/main/resources/fxml/receipt_%s_%s.txt",
                    payment.getPaymentId(),
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")));

            FileWriter writer = new FileWriter(fileName);
            writer.write(payment.generateReceipt());
            writer.close();

            System.out.println("Receipt saved to: " + fileName);
            return true;

        } catch (IOException e) {
            System.err.println("Error saving receipt: " + e.getMessage());
            return false;
        }
    }

    public static String validatePayment(double amountPaid) {
        if (amountPaid < 0) {
            return "Amount cannot be negative!";
        }
        if (amountPaid < TICKET_PRICE) {
            return String.format("Insufficient amount! Ticket price is P%.2f", TICKET_PRICE);
        }
        return null;
    }
}
