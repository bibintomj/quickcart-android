package com.example.quickcart.Model;


// PaymentDetails class to represent the payment details
public class PaymentDetails {
    private String nameOnCard;
    private String cardNumber;
    private String expiry;
    private String cvv;

    // Constructor
    public PaymentDetails(String nameOnCard, String cardNumber, String expiry, String cvv) {
        this.nameOnCard = nameOnCard;
        this.cardNumber = cardNumber;
        this.expiry = expiry;
        this.cvv = cvv;
    }

    // Getters and Setters
    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}