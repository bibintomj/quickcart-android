package com.example.quickcart.Product;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private static ProductService instance;
    private FirebaseFirestore firestore;
    private List<Product> cachedProducts = new ArrayList<>();
    private boolean isDataFetched = false;

    private ProductService() {
        firestore = FirebaseFirestore.getInstance();
    }

    public static ProductService getInstance() {
        if (instance == null) {
            instance = new ProductService();
        }
        return instance;
    }

    // Fetch products from database
    public void fetchProductsFromFirestore(FirebaseCallback callback) {
        if (!isDataFetched) {
            firestore.collection("products")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<ProductContainer> containers = queryDocumentSnapshots.toObjects(ProductContainer.class);
                        for (ProductContainer container : containers) {
                            if (container.products != null) {
                                cachedProducts.addAll(container.getProducts());

                            }
                        }

                        // Log the products
                        for (Product product : cachedProducts) {
                            Log.d("ProductList", "Product: " + product.toString());
                        }

                        isDataFetched = true;

                        callback.onSuccess(cachedProducts);
                    })
                    .addOnFailureListener(e -> {
                        callback.onFailure(e);
                    });
        } else {
            callback.onSuccess(cachedProducts);
        }
    }

    // To fetch the cached products directly
    public List<Product> getCachedProducts() {
        return cachedProducts;
    }

    public boolean isDataFetched() {
        return isDataFetched;
    }

    // Clear cache when needed (e.g., after user logs out)
    public void clearCache() {
        cachedProducts = null;
        isDataFetched = false;
    }
}

class ProductContainer {
    ArrayList<Product> products;

    public ProductContainer() {
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}