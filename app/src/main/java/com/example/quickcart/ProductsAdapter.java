package com.example.quickcart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_BANNER = 0;
    private static final int TYPE_GRID_ITEM = 1;

    private List<String> products;

    public ProductsAdapter(List<String> products) {
        this.products = products;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_BANNER : TYPE_GRID_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_BANNER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brand_banner_grid_layout, parent, false);
            return new BannerViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_grid_layout, parent, false);
            return new ProductCardHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_BANNER) {
            BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
            // Customize banner
        } else {
            ProductCardHolder productsCardHolder = (ProductCardHolder) holder;
            String product = products.get(position - 1);

            // Customize product card
        }
    }


    @Override
    public int getItemCount() {
        return products.size() + 1;
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        BannerViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class ProductCardHolder extends RecyclerView.ViewHolder {
        public ProductCardHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
