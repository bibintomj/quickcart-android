package com.example.quickcart.Product;

import java.util.List;

public interface FirebaseCallback {
    void onSuccess(List<Product> products);
    void onFailure(Exception e);
}