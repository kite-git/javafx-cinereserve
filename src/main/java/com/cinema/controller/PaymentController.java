package com.cinema.controller;

import com.cinema.model.PaymentCallback;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.cinema.util.PaymentService;

import java.net.URL;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {

    @FXML private Label movieTitleLabel;
    @FXML private Label showTimeLabel;
    @FXML private Label seatNumberLabel;
    @FXML private Label ticketPriceLabel;
    @FXML private TextField amountField;
    @FXML private Label changeLabel;
    @FXML private Button payButton;
    @FXML private Button cancelButton;
    @FXML private Label statusLabel;

    private String movieTitle;
    private String showTime;
    private String seatNumber;
    private String customerName;
    private PaymentCallback callback;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ticketPriceLabel.setText(String.format("P%.2f", PaymentService.getTicketPrice()));

        amountField.textProperty().addListener((obs, oldVal, newVal) -> {
            calculateChange();
        });

        amountField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*\\.?\\d*")) {
                amountField.setText(oldVal);
            }
        });
    }

    public void setBookingDetails(String movieTitle, String showTime, String seatNumber,
                                  String customerName, PaymentCallback callback) {
        this.movieTitle = movieTitle;
        this.showTime = showTime;
        this.seatNumber = seatNumber;
        this.customerName = customerName;
        this.callback = callback;

        movieTitleLabel.setText(movieTitle);
        showTimeLabel.setText(showTime);
        seatNumberLabel.setText(seatNumber);
    }

    private void calculateChange() {
        try {
            String amountText = amountField.getText().trim();
            if (!amountText.isEmpty()) {
                double amount = Double.parseDouble(amountText);
                double change = amount - PaymentService.getTicketPrice();

                if (change >= 0) {
                    changeLabel.setText(String.format("P%.2f", change));
                    changeLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    payButton.setDisable(false);
                } else {
                    changeLabel.setText("Insufficient amount");
                    changeLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    payButton.setDisable(true);
                }
            } else {
                changeLabel.setText("P0.00");
                changeLabel.setStyle("-fx-text-fill: black;");
                payButton.setDisable(true);
            }
        } catch (NumberFormatException e) {
            changeLabel.setText("Invalid amount");
            changeLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            payButton.setDisable(true);
        }
    }

    @FXML
    private void handlePayment() {
        try {
            double amountPaid = Double.parseDouble(amountField.getText().trim());

            String validationError = PaymentService.validatePayment(amountPaid);
            if (validationError != null) {
                statusLabel.setText(validationError);
                statusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                return;
            }

            boolean success = PaymentService.processPayment(movieTitle, showTime, seatNumber,
                    amountPaid, customerName);

            if (success) {
                statusLabel.setText("Payment successful! Receipt saved.");
                statusLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Payment Successful");
                alert.setHeaderText("Ticket Booked Successfully!");
                alert.setContentText(String.format(
                        "Movie: %s\nShow Time: %s\nSeat: %s\nAmount Paid: P%.2f\nChange: P%.2f\n\nReceipt has been saved to receipts folder.",
                        movieTitle, showTime, seatNumber, amountPaid, amountPaid - PaymentService.getTicketPrice()
                ));
                alert.showAndWait();

                if (callback != null) {
                    callback.onPaymentSuccess();
                }

                closeWindow();

            } else {
                statusLabel.setText("Payment processing failed. Please try again.");
                statusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            }

        } catch (NumberFormatException e) {
            statusLabel.setText("Please enter a valid amount!");
            statusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        }
    }

    @FXML
    private void handleCancel() {
        if (callback != null) {
            callback.onPaymentCancelled();
        }
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
