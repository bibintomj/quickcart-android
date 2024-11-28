package com.example.quickcart.OrderList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcart.Cart.CartAdapter;
import com.example.quickcart.Model.Order;
import com.example.quickcart.Model.Product;
import com.example.quickcart.Model.ProductDetails;
import com.example.quickcart.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList = new ArrayList<>();
    private final OrderAdapter.OnClickListener clickListener;

    public OrderAdapter(List<Order> orders, OrderAdapter.OnClickListener clickListener) {
        this.orderList = orders;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item_row, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClick(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void updateOrders(List<Order> orders) {
        this.orderList = orders;
        notifyDataSetChanged();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView subtitleTextView;
        TextView descriptionTextView;
        TextView priceTextView;
        TextView countTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            subtitleTextView = itemView.findViewById(R.id.subtitleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            countTextView = itemView.findViewById(R.id.countTextView);
        }

        public void bind(Order order) {
            titleTextView.setText(formatTimestamp(order.getOrderTimestamp()));
            subtitleTextView.setText("Order ID\n" + order.getOrderId());
            priceTextView.setText(String.format("$%.2f", order.getTotalAmount()));
            countTextView.setText(calculateItemCount(order.getProducts()) + " items");

            // display shipping address
            String address = "Address: " + order.getShippingAddress().getHouseNumber() +
                    ", " + order.getShippingAddress().getStreetName() +
                    ", " + order.getShippingAddress().getCity() +
                    ", " + order.getShippingAddress().getPostalCode();
//            descriptionTextView.setText(address);
        }

        private int calculateItemCount(List<ProductDetails> products) {
            int totalCount = 0;
            for (ProductDetails product : products) {
                int count = product.getCount();
                totalCount += count;
            }
            return totalCount;
        }

        private String formatTimestamp(long timestamp) {
            Date date = new Date(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            return sdf.format(date);
        }
    }

    public interface OnClickListener {
        void onClick(Order order);
    }
}
