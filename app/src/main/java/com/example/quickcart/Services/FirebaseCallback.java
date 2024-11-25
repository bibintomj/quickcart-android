package com.example.quickcart.Services;

import com.example.quickcart.Modal.Product;

import java.util.List;

public interface FirebaseCallback {
    void onSuccess(List<Product> products);
    void onFailure(Exception e);
}