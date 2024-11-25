package com.example.quickcart.Services;

import com.example.quickcart.Modal.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartService {
    private static CartService instance;
    private Map<Product, Integer> cartItems; // Product as key, Quantity as value

    private double taxPercentage = 0.13; // 13% tax

    private CartService() {
        cartItems = new HashMap<>();
    }

    public static CartService getInstance() {
        if (instance == null) {
            instance = new CartService();
        }
        return instance;
    }

    // Add a product to the cart and returns updated count
    public int addToCart(Product product) {
        if (cartItems.containsKey(product)) {
            cartItems.put(product, cartItems.get(product) + 1);
        } else {
            cartItems.put(product, 1);
        }
        return getProductQuantity(product);
    }

    // Remove a product from the cart and returns updated count
    public int removeFromCart(Product product) {
        if (cartItems.containsKey(product)) {
            int currentQuantity = cartItems.get(product);
            if (currentQuantity > 1) {
                cartItems.put(product, currentQuantity - 1);
            } else {
                cartItems.remove(product);
            }
        }
        return getProductQuantity(product);
    }

    // Get the cart products
    public List<Product> getCartProducts() {
        return new ArrayList<>(cartItems.keySet());
    }

    // Get the quantity of a specific product in the cart
    public int getProductQuantity(Product product) {
        return cartItems.getOrDefault(product, 0);
    }

    // Get all cart items
    public Map<Product, Integer> getCartItems() {
        return new HashMap<>(cartItems); // Return a copy to protect internal data
    }

    // Get total item count
    public int getTotalItemCount() {
        int total = 0;
        for (int quantity : cartItems.values()) {
            total += quantity;
        }
        return total;
    }

    // Get total price of items in the cart
    public double getTotalPrice() {
        double totalPrice = 0;
        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            totalPrice += entry.getKey().getPrice() * entry.getValue();
        }
        return totalPrice;
    }

    public double getTotalTax() {
        return getTotalPrice() * taxPercentage;
    }

    // Get total price of items in the cart
    public double getTotalPriceWithTax() {
        double total = getTotalPrice();
        return total + (total * taxPercentage);
    }

    // Clear the cart
    public void clearCart() {
        cartItems.clear();
    }
}
