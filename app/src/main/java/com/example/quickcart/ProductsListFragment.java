package com.example.quickcart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ProductsListFragment extends Fragment {

    ImageButton cartButton;
    RecyclerView productsRecyclerView;
    RecyclerView.Adapter productsAdapter;
    GridLayoutManager layoutManager;
    List<String> products = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        products.add("Bill Gates");
        products.add("Steve Jobs");
        products.add("Tim Cook");
        products.add("Elon Musk");
        products.add("Jeff Bezos");
        products.add("Mark Zuckerburg");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products_list, container, false);
        cartButton = view.findViewById(R.id.cartButton);
        productsRecyclerView = view.findViewById(R.id.productsRecyclerView);

        layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
            }
        });

        productsRecyclerView.setLayoutManager(layoutManager);
        productsAdapter = new ProductsAdapter(products);
        productsRecyclerView.setAdapter(productsAdapter);
        return view;
    }
}