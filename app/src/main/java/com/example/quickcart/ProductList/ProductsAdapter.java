package com.example.quickcart.ProductList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quickcart.R;
import com.example.quickcart.Services.CartService;
import com.example.quickcart.Modal.Product;

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
        private View starBar;

        private final Button addButton;

        private final LinearLayout countLinearLayout;
        private final Button decreaseCountButton;
        private final TextView countTextView;
        private final Button increaseCountButton;

        private final CartService cartService = CartService.getInstance();

        public ProductCardHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            starBar = itemView.findViewById(R.id.starBar);
            addButton = itemView.findViewById(R.id.addButton);
            countLinearLayout = itemView.findViewById(R.id.countLinearLayout);
            decreaseCountButton = itemView.findViewById(R.id.decreaseCountButton);
            countTextView = itemView.findViewById(R.id.countTextView);
            increaseCountButton = itemView.findViewById(R.id.increaseCountButton);
        }

        public void bind(Product product) {
            nameTextView.setText(product.getTitle());
            priceTextView.setText(String.format("$%.2f", product.getPrice()));
            descriptionTextView.setText(product.getCategory());
            if (product.getImages() != null && !product.getImages().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(product.getImages().get(0))
                        .into(productImageView);
            } else {
                Glide.with(itemView.getContext())
                        .load(R.drawable.default_image)  // Placeholder image
                        .into(productImageView);
            }

            updateStarBar(product.getRating().getRate());
            setupListenersForProductCountChange(product);
            updateViewWithProductCountInCart(CartService.getInstance().getProductQuantity(product));
        }

        private void setupListenersForProductCountChange(Product product) {
            decreaseCountButton.setOnClickListener(v -> {
                int updatedCount = CartService.getInstance().removeFromCart(product);
                updateViewWithProductCountInCart(updatedCount);
            });

            increaseCountButton.setOnClickListener(v -> {
                int updatedCount = CartService.getInstance().addToCart(product);
                updateViewWithProductCountInCart(updatedCount);
            });

            addButton.setOnClickListener(v -> {
                int updatedCount = CartService.getInstance().addToCart(product);
                updateViewWithProductCountInCart(updatedCount);
            });
        }

        private void updateViewWithProductCountInCart(int count) {
            countTextView.setText(String.valueOf(count));
            countLinearLayout.setVisibility(count == 0 ? View.GONE : View.VISIBLE);
            addButton.setVisibility(count == 0 ? View.VISIBLE : View.GONE);
        }

        private void updateStarBar(double rating) {
            ImageView[] stars = {
                    starBar.findViewById(R.id.star1),
                    starBar.findViewById(R.id.star2),
                    starBar.findViewById(R.id.star3),
                    starBar.findViewById(R.id.star4),
                    starBar.findViewById(R.id.star5)
            };

            for (int i = 0; i < stars.length; i++) {
                if (rating > i) {
                    stars[i].setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.yellow));
                } else {
                    stars[i].setColorFilter(ContextCompat.getColor(itemView.getContext(), R.color.gray));
                }
            }
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
