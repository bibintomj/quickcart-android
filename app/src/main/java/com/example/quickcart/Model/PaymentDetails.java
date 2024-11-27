package com.example.quickcart.Model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

// PaymentDetails class to represent the payment details
public class PaymentDetails implements Parcelable {
    private String nameOnCard;
    private String cardNumber;
    private String expiry;
    private String cvv;

    public PaymentDetails() {
    }

    // Constructor
    public PaymentDetails(String nameOnCard, String cardNumber, String expiry, String cvv) {
        this.nameOnCard = nameOnCard;
        this.cardNumber = cardNumber;
        this.expiry = expiry;
        this.cvv = cvv;
    }

    protected PaymentDetails(Parcel in) {
        nameOnCard = in.readString();
        cardNumber = in.readString();
        expiry = in.readString();
        cvv = in.readString();
    }

    public static final Creator<PaymentDetails> CREATOR = new Creator<PaymentDetails>() {
        @Override
        public PaymentDetails createFromParcel(Parcel in) {
            return new PaymentDetails(in);
        }

        @Override
        public PaymentDetails[] newArray(int size) {
            return new PaymentDetails[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(nameOnCard);
        parcel.writeString(cardNumber);
        parcel.writeString(expiry);
        parcel.writeString(cvv);
    }
}