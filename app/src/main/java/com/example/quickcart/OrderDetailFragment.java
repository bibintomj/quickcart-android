package com.example.quickcart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.quickcart.Model.Order;
import com.example.quickcart.Model.Product;
import com.example.quickcart.ProductList.ProductsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class OrderDetailFragment extends Fragment {
    ImageButton backButton;
    TextView titleTextView;
    RecyclerView orderDetailRecyclerView;
//
//    private OrderDetailAdapter orderDetailAdapter;
//    private GridLayoutManager layoutManager;
//    private Order order;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            order = getArguments().getParcelable("order");
//        }
//        if (order != null) {
//            populateOrderData();
//        } else {
//            Log.e("ProductDetailFragment", "Product is null");
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);
        backButton = view.findViewById(R.id.backButton);
        titleTextView = view.findViewById(R.id.titleTextView);
        orderDetailRecyclerView = view.findViewById(R.id.orderDetailRecyclerView);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });

        return view;
    }
}