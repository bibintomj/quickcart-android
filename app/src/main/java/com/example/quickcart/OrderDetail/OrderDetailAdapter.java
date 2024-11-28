package com.example.quickcart.OrderDetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickcart.Model.ProductDetails;
import com.example.quickcart.R;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {

    private List<ProductDetails> productList;

    public OrderDetailAdapter(List<ProductDetails> productList) {
        this.productList = productList;
    }

    public void updateOrders(List<ProductDetails> newProductList) {
        this.productList = newProductList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_product_item_row, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        ProductDetails product = productList.get(position);

        holder.titleTextView.setText(product.getTitle());
        holder.subtitleTextView.setText(String.format("$%.2f", product.getPrice()) + " x " + product.getCount());
        holder.priceTextView.setText(String.format("$%.2f", product.getPrice() * product.getCount()));
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public static class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView subtitleTextView;
        TextView priceTextView;

        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            subtitleTextView = itemView.findViewById(R.id.subtitleTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
        }
    }
}