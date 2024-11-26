package com.example.quickcart.Model;

import java.util.List;
import java.util.Map;

public class Order {
    private String userId;
    private ShippingAddress shippingAddress;
    private PaymentDetails paymentDetails;
    private double totalAmount;
    private List<Map<String, Object>> products; // Each product contains id, count, and price
    private String status; // Example: "Pending", "Completed"
    private long orderTimestamp;

    // Constructor
    public Order(String userId, ShippingAddress shippingAddress, PaymentDetails paymentDetails,
                 double totalAmount, List<Map<String, Object>> products, String status, long orderTimestamp) {
        this.userId = userId;
        this.shippingAddress = shippingAddress;
        this.paymentDetails = paymentDetails;
        this.totalAmount = totalAmount;
        this.products = products;
        this.status = status;
        this.orderTimestamp = orderTimestamp;
    }

    // Getters and Setters
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

    public List<Map<String, Object>> getProducts() {
        return products;
    }

    public void setProducts(List<Map<String, Object>> products) {
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
}
