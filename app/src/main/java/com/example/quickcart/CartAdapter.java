package com.example.quickcart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quickcart.Product.CartService;
import com.example.quickcart.Product.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SUBTOTAL = 0;
    private static final int TYPE_ROW_ITEM = 1;

    private List<Product> products;
    private final OnProductClickListener clickListener;
    private final OnCartUpdateListener cartUpdateListener;

    @Override
    public int getItemViewType(int position) {
        return position == products.size() ? TYPE_SUBTOTAL : TYPE_ROW_ITEM;
    }

    public CartAdapter(List<Product> products, OnProductClickListener clickListener, OnCartUpdateListener cartUpdateListener) {
        this.products = products;
        this.clickListener = clickListener;
        this.cartUpdateListener = cartUpdateListener;
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
            subTotalViewHolder.bind();
        } else {
            CartItemViewHolder cartItemViewHolder = (CartItemViewHolder) holder;
            Product product = products.get(position);
            cartItemViewHolder.bind(product);
            cartItemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onProductClick(product);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (products.size() == 0 ? 0 : products.size() + 1);
    }

    // Method to update the product list and notify the adapter
    public void updateProductList(List<Product> newProducts) {
        this.products = newProducts;
        notifyDataSetChanged();
    }

    static class SubTotalViewHolder extends RecyclerView.ViewHolder {
        TextView subtotalTextView;
        TextView taxTextView;
        TextView estimatedTotalTextView;

        CartService cartService = CartService.getInstance();

        SubTotalViewHolder(View itemView) {
            super(itemView);
            subtotalTextView = itemView.findViewById(R.id.subtotalTextView);
            taxTextView = itemView.findViewById(R.id.taxTextView);
            estimatedTotalTextView = itemView.findViewById(R.id.estimatedTotalTextView);
        }

        public void bind() {
            subtotalTextView.setText(String.format("$%.2f", cartService.getTotalPrice()));
            taxTextView.setText(String.format("$%.2f", cartService.getTotalTax()));
            estimatedTotalTextView.setText(String.format("$%.2f", cartService.getTotalPriceWithTax()));
        }
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageView;

        TextView nameTextView;
        TextView descriptionTextView;

        Button decreaseCountButton;
        TextView countTextView;
        Button increaseCountButton;

        TextView priceTextView;

        CartService cartService = CartService.getInstance();

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.productImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            decreaseCountButton = itemView.findViewById(R.id.decreaseCountButton);
            countTextView = itemView.findViewById(R.id.countTextView);
            increaseCountButton = itemView.findViewById(R.id.increaseCountButton);
            priceTextView = itemView.findViewById(R.id.priceTextView);
        }

        public void bind(Product product) {
            if (product.getImages() != null && !product.getImages().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(product.getImages().get(0))
                        .into(productImageView);
            } else {
                Glide.with(itemView.getContext())
                        .load(R.drawable.default_image)  // Placeholder image
                        .into(productImageView);
            }

            nameTextView.setText(product.getTitle());
            int count = cartService.getProductQuantity(product);
            updateViewWithProductCountInCart(product, count);

            setupListenersForProductCountChange(product);
        }

        private void setupListenersForProductCountChange(Product product) {
            decreaseCountButton.setOnClickListener(v -> {
                int updatedCount = CartService.getInstance().removeFromCart(product);
                updateViewWithProductCountInCart(product, updatedCount);
                cartUpdateListener.onCartUpdated();
            });

            increaseCountButton.setOnClickListener(v -> {
                int updatedCount = CartService.getInstance().addToCart(product);
                updateViewWithProductCountInCart(product, updatedCount);
                cartUpdateListener.onCartUpdated();
            });
        }

        private void updateViewWithProductCountInCart(Product product, int count) {
            descriptionTextView.setText(count + " x $" + product.getPrice());
            countTextView.setText(String.valueOf(count));
            priceTextView.setText(String.format("$%.2f", product.getPrice() * count));
        }
    }

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    // Add interface for the cart update listener
    public interface OnCartUpdateListener {
        void onCartUpdated();
    }
}
