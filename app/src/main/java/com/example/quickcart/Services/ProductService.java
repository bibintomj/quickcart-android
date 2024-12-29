package com.example.quickcart.Services;

import android.content.Context;
import android.util.Log;

import com.example.quickcart.Model.Product;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
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

    public void loadProducts(FirebaseCallback callback, Context context) {
        Boolean loadFromLocal = true;
        if (loadFromLocal) {
            // Loading from local JSON (products.json located in root folder). THIS IS FOR DEMO PURPOSES.
            // To Firestore crash error, you need to insert products to your firestore in "products" collection, and update the google-services.json file.
            // Firestore console does not support import or export data as of 29 Dec 2024.
            // To insert into firestore you can use the npm package https://www.npmjs.com/package/node-firestore-import-export
            //      > Get credentials json from firebase console > Project Settings > Service Accounts > Generate new private key
            //      > Commands for import and export - https://stackoverflow.com/a/62718371/8193339
            cachedProducts.addAll(loadProductsFromLocalJson(context));
            callback.onSuccess(cachedProducts);
        } else {
            fetchProductsFromFirestore(callback);
        }

    }

    // Fetch products from database
    private void fetchProductsFromFirestore(FirebaseCallback callback) {
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

    private List<Product> loadProductsFromLocalJson(Context context) {
        StringBuilder json = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.getAssets().open("products.json")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        ProductContainer container = gson.fromJson(json.toString(), ProductContainer.class);

        // Handle null case to avoid NullPointerException
        return container != null && container.getProducts() != null ? container.getProducts() : new ArrayList<>();
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