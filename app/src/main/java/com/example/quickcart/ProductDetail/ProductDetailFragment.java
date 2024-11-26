package com.example.quickcart.ProductDetail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.quickcart.Services.CartService;
import com.example.quickcart.Model.Product;
import com.example.quickcart.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ProductDetailFragment extends Fragment {

    private ImageButton backButton;
    private TextView titleTextView;
    private ImageButton cartButton;

    private ViewPager2 imagePageViewer2;
    RecyclerView.Adapter adapter;

    private TextView priceTextView;
    private TextView productNameTextView;
    private TextView soldByTextView;

    private View starBar;

    private TextView returnTextView;
    private TextView productDescription;

    private LinearLayout countLinearLayout;
    private Button decreaseCountButton;
    private TextView countTextView;
    private Button increaseCountButton;

    private Button addButton;

    private Product product;

    private CartService cartService = CartService.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        extractComponentReferencesInView(view);
        setupListenersOnView(view);
        Log.i("ABC","onCreateView");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            product = getArguments().getParcelable("product");
        }
        if (product != null) {
            populateProductData();
        } else {
            Log.e("ProductDetailFragment", "Product is null");
        }
    }

    private void populateProductData() {
        adapter = new ProductImageAdapter(product.getImages());
        imagePageViewer2.setAdapter(adapter);
        priceTextView.setText(product.getPriceFormattedString());
        productNameTextView.setText(product.getTitle());;
        soldByTextView.setText("");
        updateStarBar(product.getRating().getRate());
        returnTextView.setText("");
        productDescription.setText(product.getDescription());

        setupListenersForProductCountChange(product);
        updateViewWithProductCountInCart(cartService.getProductQuantity(product));
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

        imagePageViewer2 = view.findViewById(R.id.productImageViewPager2);
        priceTextView = view.findViewById(R.id.priceTextView);
        productNameTextView = view.findViewById(R.id.productNameTextView);
        soldByTextView = view.findViewById(R.id.soldByTextView);
        starBar = view.findViewById(R.id.starBar);
        returnTextView = view.findViewById(R.id.returnTextView);
        productDescription = view.findViewById(R.id.productDescription);
        countLinearLayout = view.findViewById(R.id.countLinearLayout);
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
}