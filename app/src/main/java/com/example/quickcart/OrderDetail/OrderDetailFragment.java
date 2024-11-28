package com.example.quickcart.OrderDetail;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.quickcart.Model.Order;
import com.example.quickcart.R;
import com.example.quickcart.Services.CartService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class OrderDetailFragment extends Fragment {
    ImageButton backButton;
    TextView titleTextView;
    RecyclerView orderDetailRecyclerView;

    TextView totalTextView;
    TextView orderIdTextView;
    TextView dateTextView;
    TextView addressTextView;

    private OrderDetailAdapter orderDetailAdapter;
    private LinearLayoutManager layoutManager;
    private Order order;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            order = getArguments().getParcelable("order");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);
        backButton = view.findViewById(R.id.backButton);
        titleTextView = view.findViewById(R.id.titleTextView);
        orderDetailRecyclerView = view.findViewById(R.id.orderDetailRecyclerView);

        totalTextView = view.findViewById(R.id.totalTextView);
        orderIdTextView = view.findViewById(R.id.orderIdTextView);
        dateTextView = view.findViewById(R.id.dateTextView);
        addressTextView = view.findViewById(R.id.addressTextView);

        // Layout manager based on orientation
        layoutManager = new LinearLayoutManager(requireContext());
        orderDetailRecyclerView.setLayoutManager(layoutManager);

        // Create and add the divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                orderDetailRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL
        );
        orderDetailRecyclerView.addItemDecoration(dividerItemDecoration);

        orderDetailAdapter = new OrderDetailAdapter(order.getProducts());
        orderDetailRecyclerView.setAdapter(orderDetailAdapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });

        populateData();
        return view;
    }

    private void populateData() {
        if (order == null) {
            return;
        }

        totalTextView.setText(String.format("$%.2f", order.getTotalAmount()));
        orderIdTextView.setText(order.getOrderId());
        dateTextView.setText(formatTimestamp(order.getOrderTimestamp()));

        // display shipping address
        String address = order.getShippingAddress().getHouseNumber() +
                ", " + order.getShippingAddress().getStreetName() +
                "\n" + order.getShippingAddress().getCity() +
                ", " + order.getShippingAddress().getPostalCode();

        addressTextView.setText(address);
        orderDetailAdapter.updateOrders(order.getProducts());
    }

    private String formatTimestamp(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy 'at' HH:mm a", Locale.getDefault());
        return sdf.format(date);
    }
}