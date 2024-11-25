package com.example.quickcart.ProductDetail;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.quickcart.R;

import java.util.ArrayList;

public class ProductImageAdapter extends RecyclerView.Adapter<ProductImageAdapter.MyViewHolder> {
    private ArrayList<String> productImages;

    public ProductImageAdapter(ArrayList<String> cellList) {
        this.productImages = cellList;
    }

    @NonNull
    @Override
    public ProductImageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductImageAdapter.MyViewHolder holder, int position) {
        String imageUrl = productImages.get(position);
        holder.bind(imageUrl);
    }

    @Override
    public int getItemCount() {
        return productImages.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public MyViewHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.product_image_view, parent, false));
            imageView = itemView.findViewById(R.id.imageView);
        }

        public void bind(String imageUrl) {
            Glide.with(itemView.getContext())
                    .load(imageUrl)
                    .into(imageView);
        }
    }
}
