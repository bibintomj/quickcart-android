package com.example.quickcart.Services;

import com.example.quickcart.Model.Order;
import com.example.quickcart.Model.PaymentDetails;
import com.example.quickcart.Model.Product;
import com.example.quickcart.Model.ShippingAddress;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderService {
    private static OrderService instance;

    private FirebaseFirestore db;

    private OrderService() {
        db = FirebaseFirestore.getInstance();
    }

    public static OrderService getInstance() {
        if (instance == null) {
            instance = new OrderService();
        }
        return instance;
    }

    public interface OrderCallback {
        void onSuccess();

        void onFailure(String errorMessage);
    }

    public void placeOrder(String userId, ShippingAddress shippingAddress, PaymentDetails paymentDetails,
                                        Map<Product, Integer> cartItems, double totalAmount,  OrderCallback callback) {

        List<Map<String, Object>> productDetails = new ArrayList<>();
        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            Product product = entry.getKey();
            Integer count = entry.getValue();

            Map<String, Object> productMap = new HashMap<>();
            productMap.put("id", product.getId());
            productMap.put("title", product.getTitle());
            productMap.put("count", count);
            productMap.put("price", product.getPrice());
            productDetails.add(productMap);
        }

        Order order = new Order(userId, shippingAddress,
                                paymentDetails, totalAmount,
                                productDetails, "Pending",
                                System.currentTimeMillis());

        // writing to firestore (placing order)
        db.collection("orders")
                .add(order)
                .addOnSuccessListener(documentReference -> {
                    if (callback != null) {
                        callback.onSuccess();
                    }
                })
                .addOnFailureListener(e -> {
                    if (callback != null) {
                        callback.onFailure(e.getMessage());
                    }
                });
    }
}
