package com.example.quickcart.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Order implements Parcelable {
    private String orderId;
    private String userId;
    private ShippingAddress shippingAddress;
    private PaymentDetails paymentDetails;
    private double totalAmount;
    private List<ProductDetails> products; // Each product contains id, count, and price
    private String status; // Example: "Pending", "Completed"
    private long orderTimestamp;

    public Order() {
    }

    // Constructor
    public Order(String userId, ShippingAddress shippingAddress, PaymentDetails paymentDetails,
                 double totalAmount, List<ProductDetails> products, String status, long orderTimestamp) {
        this.orderId = UUID.randomUUID().toString();
        this.userId = userId;
        this.shippingAddress = shippingAddress;
        this.paymentDetails = paymentDetails;
        this.totalAmount = totalAmount;
        this.products = products;
        this.status = status;
        this.orderTimestamp = orderTimestamp;
    }

    protected Order(Parcel in) {
        orderId = in.readString();
        userId = in.readString();
        shippingAddress = in.readParcelable(ShippingAddress.class.getClassLoader());
        paymentDetails = in.readParcelable(PaymentDetails.class.getClassLoader());
        totalAmount = in.readDouble();
        status = in.readString();
        orderTimestamp = in.readLong();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId() {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<ProductDetails> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDetails> products) {
        this.products = products;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getOrderTimestamp() {
        return orderTimestamp;
    }

    public void setOrderTimestamp(long orderTimestamp) {
        this.orderTimestamp = orderTimestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(orderId);
        parcel.writeString(userId);
        parcel.writeParcelable(shippingAddress, i);
        parcel.writeParcelable(paymentDetails, i);
        parcel.writeDouble(totalAmount);
        parcel.writeTypedList(products);
        parcel.writeString(status);
        parcel.writeLong(orderTimestamp);
    }
}
