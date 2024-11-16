package com.example.quickcart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SUBTOTAL = 0;
    private static final int TYPE_ROW_ITEM = 1;

    private List<String> products;
    private OnProductClickListener clickListener;

    @Override
    public int getItemViewType(int position) {
        return position == products.size() ? TYPE_SUBTOTAL : TYPE_ROW_ITEM;
    }

    public CartAdapter(List<String> products, OnProductClickListener clickListener) {
        this.products = products;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SUBTOTAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_total, parent, false);
            return new SubTotalViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_row, parent, false);
            return new CartItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_SUBTOTAL) {
            SubTotalViewHolder subTotalViewHolder = (SubTotalViewHolder) holder;
            // Customize banner
        } else {
            CartItemViewHolder cartItemViewHolder = (CartItemViewHolder) holder;
            String product = products.get(position);
            cartItemViewHolder.bind(product);
            cartItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onProductClick(product);
                }
            });
            // Customize product card
        }
    }

    @Override
    public int getItemCount() {
        return products.size() + 1;
    }

    static class SubTotalViewHolder extends RecyclerView.ViewHolder {
        SubTotalViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class CartItemViewHolder extends RecyclerView.ViewHolder {
        // Declare all items in field
        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // initialize all items in field
        }

        public void bind(String product) {
            // Set prduct detail to the card cell
        }
    }

    public interface OnProductClickListener {
        void onProductClick(String product);
    }
}
