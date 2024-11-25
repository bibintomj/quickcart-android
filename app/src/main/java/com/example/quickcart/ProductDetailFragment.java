package com.example.quickcart;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ProductDetailFragment extends Fragment {

    private ImageButton backButton;
    private TextView titleTextView;
    private ImageButton cartButton;

    private ImageView bannerImageView;
    private TextView priceTextView;
    private TextView productNameTextView;
    private TextView soldByTextView;

    private View starBar;

    private TextView returnTextView;
    private TextView productDescription;

    private Button decreaseCountButton;
    private TextView countTextView;
    private Button increaseCountButton;

    private Button addButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        String productName = getArguments().getString("product", "No Product");
        extractComponentReferencesInView(view);
        setupListenersOnView(view);
        updateStarBar(4);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void updateStarBar(float rating) {
        ImageView[] stars = {
                starBar.findViewById(R.id.star1),
                starBar.findViewById(R.id.star2),
                starBar.findViewById(R.id.star3),
                starBar.findViewById(R.id.star4),
                starBar.findViewById(R.id.star5)
        };

        for (int i = 0; i < stars.length; i++) {
            if (rating > i) {
                stars[i].setColorFilter(ContextCompat.getColor(requireContext(), R.color.yellow));
            } else {
                stars[i].setColorFilter(ContextCompat.getColor(requireContext(), R.color.gray));
            }
        }
    }

    public void extractComponentReferencesInView(View view) {
        backButton = view.findViewById(R.id.backButton);
        titleTextView = view.findViewById(R.id.titleTextView);
        cartButton = view.findViewById(R.id.cartButton);

        bannerImageView = view.findViewById(R.id.bannerImageView);
        priceTextView = view.findViewById(R.id.priceTextView);
        productNameTextView = view.findViewById(R.id.productNameTextView);
        soldByTextView = view.findViewById(R.id.soldByTextView);
        starBar = view.findViewById(R.id.starBar);
        returnTextView = view.findViewById(R.id.returnTextView);
        productDescription = view.findViewById(R.id.productDescription);
        decreaseCountButton = view.findViewById(R.id.decreaseCountButton);
        countTextView = view.findViewById(R.id.countTextView);
        increaseCountButton = view.findViewById(R.id.increaseCountButton);
        addButton = view.findViewById(R.id.addButton);
    }

    public void setupListenersOnView(View view) {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                Navigation.findNavController(view).popBackStack();
            }
        });
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                Navigation.findNavController(view).navigate(R.id.navigateToCart);
            }
        });
    }
}