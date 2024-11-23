package com.example.quickcart;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quickcart.Product.Product;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_BANNER = 0;
    private static final int TYPE_GRID_ITEM = 1;

    private final List<Product> products;
    private final OnProductClickListener clickListener;

    public ProductsAdapter(List<Product> products, OnProductClickListener clickListener) {
        this.products = products;
        this.clickListener = clickListener;
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
            // Customize banner if needed
        } else {
            ProductCardHolder productCardHolder = (ProductCardHolder) holder;
            Product product = products.get(position - 1); // Subtract 1 to account for the banner
            productCardHolder.bind(product);
            productCardHolder.itemView.setOnClickListener(view -> clickListener.onProductClick(product));
        }
    }

    @Override
    public int getItemCount() {
        return products.size() + 1; // +1 for the banner
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        BannerViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class ProductCardHolder extends RecyclerView.ViewHolder {
        private final ImageView productImageView;
        private final TextView priceTextView;
        private final TextView nameTextView;
        private final TextView descriptionTextView;
        private final Button addButton;

        private final LinearLayout countLinearLayout;
        private final Button decreaseCountButton;
        private final TextView countTextView;
        private final Button increaseCountButton;



        public ProductCardHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            addButton = itemView.findViewById(R.id.addButton);
            countLinearLayout = itemView.findViewById(R.id.countLinearLayout);
            decreaseCountButton = itemView.findViewById(R.id.decreaseCountButton);
            countTextView = itemView.findViewById(R.id.countTextView);
            increaseCountButton = itemView.findViewById(R.id.increaseCountButton);
        }

        public void bind(Product product) {
            nameTextView.setText(product.getTitle());
            priceTextView.setText(String.format("$%.2f", product.getPrice()));
//            descriptionTextView.setText(product.getDescription());
            if (product.getImages() != null && !product.getImages().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(product.getImages().get(0))
                        .into(productImageView);
            } else {
                // Handle the case where images are empty or null
                // You can load a placeholder image or do something else
                Glide.with(itemView.getContext())
                        .load(R.drawable.default_image)  // Placeholder image
                        .into(productImageView);
            }

            descriptionTextView.setVisibility(View.GONE);
            countLinearLayout.setVisibility(View.GONE);
            addButton.setOnClickListener(v -> {
                Toast.makeText(itemView.getContext(), "Added to Cart" + product.getTitle(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public void updateProducts(List<Product> newProducts) {
        this.products.clear();
        this.products.addAll(newProducts);
        notifyDataSetChanged(); // reloading the recylcer view
    }
}
