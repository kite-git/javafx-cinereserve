package com.cinema.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Payment {
    private String paymentId;
    private String movieTitle;
    private String showTime;
    private String seatNumber;
    private double ticketPrice;
    private double amountPaid;
    private double change;
    private LocalDateTime paymentDate;
    private String customerName;

    public Payment(String paymentId, String movieTitle, String showTime, String seatNumber,
                   double ticketPrice, double amountPaid, String customerName) {
        this.paymentId = paymentId;
        this.movieTitle = movieTitle;
        this.showTime = showTime;
        this.seatNumber = seatNumber;
        this.ticketPrice = ticketPrice;
        this.amountPaid = amountPaid;
        this.change = amountPaid - ticketPrice;
        this.paymentDate = LocalDateTime.now();
        this.customerName = customerName;
    }

    public String getPaymentId() { return paymentId; }
    public String getMovieTitle() { return movieTitle; }
    public String getShowTime() { return showTime; }
    public String getSeatNumber() { return seatNumber; }
    public double getTicketPrice() { return ticketPrice; }
    public double getAmountPaid() { return amountPaid; }
    public double getChange() { return change; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public String getCustomerName() { return customerName; }

    public String getFormattedDate() {
        return paymentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String generateReceipt() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("=====================================\n");
        receipt.append("         CINERESERVE CINEMA          \n");
        receipt.append("            TICKET RECEIPT           \n");
        receipt.append("=====================================\n\n");

        receipt.append("Payment ID: ").append(paymentId).append("\n");
        receipt.append("Date & Time: ").append(getFormattedDate()).append("\n");
        receipt.append("Customer: ").append(customerName).append("\n\n");

        receipt.append("MOVIE DETAILS:\n");
        receipt.append("Title: ").append(movieTitle).append("\n");
        receipt.append("Show Time: ").append(showTime).append("\n");
        receipt.append("Seat: ").append(seatNumber).append("\n\n");

        receipt.append("PAYMENT DETAILS:\n");
        receipt.append("Ticket Price: P").append(String.format("%.2f", ticketPrice)).append("\n");
        receipt.append("Amount Paid: P").append(String.format("%.2f", amountPaid)).append("\n");
        receipt.append("Change: P").append(String.format("%.2f", change)).append("\n\n");

        receipt.append("=====================================\n");
        receipt.append("    Thank you for choosing us!       \n");
        receipt.append("         Enjoy your movie!           \n");
        receipt.append("=====================================\n");

        return receipt.toString();
    }
}