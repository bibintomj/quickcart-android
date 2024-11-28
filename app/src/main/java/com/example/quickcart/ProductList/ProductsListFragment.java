package com.example.quickcart.ProductList;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.quickcart.R;
import com.example.quickcart.Services.CartService;
import com.example.quickcart.Services.FirebaseCallback;
import com.example.quickcart.Model.Product;
import com.example.quickcart.Services.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ProductsListFragment extends Fragment {
    private ImageButton cartButton;
    private RecyclerView productsRecyclerView;
    private ProductsAdapter productsAdapter;
    private GridLayoutManager layoutManager;
    private List<Product> products = new ArrayList<>();

    private ProductService productService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products_list, container, false);
        cartButton = view.findViewById(R.id.cartButton);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.navigateToCart);
            }
        });

        productService = ProductService.getInstance();

        productsRecyclerView = view.findViewById(R.id.productsRecyclerView);

        layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
            }
        });

        productsRecyclerView.setLayoutManager(layoutManager);
        productsAdapter = new ProductsAdapter(products, product -> {
            this.navigateToProductDetail(product);
        });

        productsRecyclerView.setAdapter(productsAdapter);

        initializeTabBar(view);

        loadProductData();
        return view;
    }

    private void initializeTabBar(View view) {
        LinearLayout tabItem1 = view.findViewById(R.id.tab_item_1);
        LinearLayout tabItem2 = view.findViewById(R.id.tab_item_2);

        tabItem1.setSelected(true);

        // Tab item 2 (More)
        tabItem2.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.navigateToMore);
        });
    }

    private void navigateToProductDetail(Product product) {
        if (getView() == null) {
            Log.e("navigation error", "cannot navigate because root view is null.");
            return;
        }
        NavController navController = Navigation.findNavController(this.getView());
        Bundle bundle = new Bundle();
        bundle.putParcelable("product", product);
        navController.navigate(R.id.navigateToProductDetail, bundle);
    }

    private void loadProductData() {
        if (productService.isDataFetched()) {
            // Use cached data
            productsAdapter.updateProducts(productService.getCachedProducts());
        } else {
            // Fetch from Firestore
            productService.fetchProductsFromFirestore(new FirebaseCallback() {
                @Override
                public void onSuccess(List<Product> products) {
                    CartService.getInstance().fetchCartFromFirebase(getContext(), new CartService.FetchCartCallback() {
                        @Override
                        public void onCartFetched(Map<Product, Integer> cartItems) {
                            productsAdapter.updateProducts(products);
                        }

                        @Override
                        public void onError(String error) {
                            productsAdapter.updateProducts(products);
                        }
                    });
                }

                @Override
                public void onFailure(Exception e) {
                    // Handle failure (e.g., show a toast or an error message)
                    Toast.makeText(getContext(), "Failed to fetch products", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}