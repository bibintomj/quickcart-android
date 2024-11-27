package com.example.quickcart.OrderList;

import android.content.res.Configuration;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.quickcart.Cart.CartAdapter;
import com.example.quickcart.Model.Order;
import com.example.quickcart.Model.Product;
import com.example.quickcart.ProductList.ProductsAdapter;
import com.example.quickcart.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class OrderListFragment extends Fragment {
    private ImageButton backButton;
    private RecyclerView ordersRecyclerView;
    private LinearLayout noContentLinearLayout;
    private Button noContentPlaceOrderButton;

    private OrderAdapter orderAdapter;
    private GridLayoutManager layoutManager;
    private List<Order> orders = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Update the span count based on orientation
        int spanCount = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 1;
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), spanCount);
        ordersRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        backButton = view.findViewById(R.id.backButton);
        ordersRecyclerView = view.findViewById(R.id.ordersRecyclerView);
        noContentLinearLayout = view.findViewById(R.id.noContentLinearLayout);
        noContentPlaceOrderButton = view.findViewById(R.id.noContentPlaceOrderButton);
        // Layout manager based on orientation
        int orientation = getResources().getConfiguration().orientation;
        layoutManager = new GridLayoutManager(requireContext(), orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 1);
        ordersRecyclerView.setLayoutManager(layoutManager);

        // Setup adapter
        orderAdapter = new OrderAdapter(orders, order -> {
            NavController navController = Navigation.findNavController(view);
            Bundle bundle = new Bundle();
            bundle.putParcelable("order", order);
            navController.navigate(R.id.navigateToOrderDetailFragment, bundle);
        });

        ordersRecyclerView.setAdapter(orderAdapter);

        setupListeners();

        loadOrders();
        return view;
    }

    private void setupListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });

        noContentPlaceOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack(R.id.productsListFragment, false);
            }
        });
    }

    private void loadOrders() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("orders").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<Order> orderList = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Order order = documentSnapshot.toObject(Order.class);
                            orderList.add(order);
                        }
                        // Pass the data to RecyclerView
                        orderAdapter.updateOrders(orderList);
                        noContentLinearLayout.setVisibility(orderList.isEmpty() ? View.VISIBLE : View.INVISIBLE);
                        ordersRecyclerView.setVisibility(orderList.isEmpty() ? View.INVISIBLE : View.VISIBLE);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error fetching orders", e);
                });
    }
}