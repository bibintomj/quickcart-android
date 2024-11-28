package com.example.quickcart.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.quickcart.Model.Product;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartService {
    private static CartService instance;
    private Map<Product, Integer> cartItems; // Product as key, Quantity as value
    private FirebaseFirestore db;  // Firestore instance
    private SharedPreferences sharedPreferences;

    private double taxPercentage  = 0.13; // 13% tax

    // This is to prevent multiple write operations to cart
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable saveCartRunnable;
    private long lastSaveTime = 0L;

    private CartService() {
        cartItems = new HashMap<>();
        db = FirebaseFirestore.getInstance();
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
        saveCartToFirebase();
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
        saveCartToFirebase();
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
        saveCartToFirebase();
    }

    // Save the cart to Firebase
    private void saveCartToFirebase() {
        long currentTime = System.currentTimeMillis();
        long delay = 10000; // 10 seconds in milliseconds

        if (saveCartRunnable != null) {
            // Remove any previously queued tasks
            handler.removeCallbacks(saveCartRunnable);
        }

        // Define the runnable to save the cart
        saveCartRunnable = () -> {
            String userId = sharedPreferences.getString("userId", null); // Retrieve the userId from SharedPreferences

            if (userId != null) {
                DocumentReference userCartRef = db.collection("users").document(userId).collection("cart").document("cartItems");

                Map<String, Object> cartData = new HashMap<>();
                for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
                    cartData.put(String.valueOf(entry.getKey().getId()), entry.getValue()); // Assuming Product has an ID
                }

                // First clear the document, then set the new data
                userCartRef.delete()  // Clear the existing cart data in Firebase
                        .addOnSuccessListener(aVoid -> {
                            // Now set the new cart data
                            userCartRef.set(cartData)
                                    .addOnSuccessListener(aVoid2 -> lastSaveTime = System.currentTimeMillis()) // Update the last save time
                                    .addOnFailureListener(e -> Log.e("SaveCart", "Failed to save cart: " + e.getMessage()));
                        })
                        .addOnFailureListener(e -> Log.e("SaveCart", "Failed to clear cart: " + e.getMessage()));
            }
        };

        // Schedule the save operation only if enough time has passed since the last save
        long timeSinceLastSave = currentTime - lastSaveTime;
        if (timeSinceLastSave >= delay) {
            // Execute immediately if 10 seconds have passed
            handler.post(saveCartRunnable);
        } else {
            // Schedule for the remaining time
            handler.postDelayed(saveCartRunnable, delay - timeSinceLastSave);
        }
    }


    public void fetchCartFromFirebase(Context context, FetchCartCallback callback) {
        sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null); // Retrieve the userId from SharedPreferences

        if (userId != null) {
            DocumentReference userCartRef = db.collection("users").document(userId).collection("cart").document("cartItems");

            userCartRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Map<String, Object> cartData = (Map<String, Object>) documentSnapshot.getData();
                    if (cartData != null) {
                        cartItems.clear();
                        for (Map.Entry<String, Object> entry : cartData.entrySet()) {
                            String productIdString = entry.getKey();
                            Integer quantity = ((Long) entry.getValue()).intValue(); // Convert to int

                            // Convert productIdString to an int
                            int productId = Integer.parseInt(productIdString);

                            // Assuming you have a method to get Product by ID
                            Product product = getProductById(productId);
                            if (product != null) {
                                cartItems.put(product, quantity);
                            }
                        }
                        // Callback after the cart is successfully fetched
                        callback.onCartFetched(cartItems);
                    } else {
                        callback.onError("Cart data is empty.");
                    }
                } else {
                    callback.onError("Cart not found for the user.");
                }
            }).addOnFailureListener(e -> {
                // Callback on failure
                callback.onError("Failed to fetch cart: " + e.getMessage());
            });
        } else {
            callback.onError("User ID not found in shared preferences.");
        }
    }


    // Get Product by ID (You can fetch from your local database or network)
    private Product getProductById(int productId) {
        // Get the list of cached products from ProductService
        List<Product> cachedProducts = ProductService.getInstance().getCachedProducts();

        // Iterate through the list of cached products to find the one that matches the productId
        for (Product product : cachedProducts) {
            if (product.getId() == productId) {
                return product; // Return the product if it matches the ID
            }
        }

        // Return null if no matching product is found
        return null;
    }

    public interface FetchCartCallback {
        void onCartFetched(Map<Product, Integer> cartItems); // When the cart is successfully fetched
        void onError(String error); // When an error occurs
    }
}
