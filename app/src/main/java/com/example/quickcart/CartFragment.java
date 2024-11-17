package com.example.quickcart;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    private ImageButton backButton;
    private TextView titleTextView;

    private LinearLayout noContentLinearLayout;
    private Button noContentContinueShoppingButton;

    private LinearLayout ctaLinearLayout;
    private Button continueShoppingButton;
    private Button checkoutButton;

    private RecyclerView cartRecyclerView;
    private RecyclerView.Adapter cartAdapter;
    private GridLayoutManager layoutManager;
    private List<String> products = new ArrayList<>();

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Update the span count based on orientation
        int spanCount = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 1;
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), spanCount);
        cartRecyclerView.setLayoutManager(layoutManager);
    }


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
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        backButton = view.findViewById(R.id.backButton);
        continueShoppingButton = view.findViewById(R.id.continueShoppingButton);
        titleTextView = view.findViewById(R.id.titleTextView);
        checkoutButton = view.findViewById(R.id.checkoutButton);
        ctaLinearLayout = view.findViewById(R.id.ctaLinearLayout);
        noContentContinueShoppingButton = view.findViewById(R.id.noContentContinueShoppingButton);
        noContentLinearLayout = view.findViewById(R.id.noContentLinearLayout);
        cartRecyclerView = view.findViewById(R.id.cartRecyclerView);
        setupListenersOnView(view);

        int orientation = getResources().getConfiguration().orientation;
        layoutManager = new GridLayoutManager(requireContext(), orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 1);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int span = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 1;
                return position == products.size() ? span : 1;
            }
        });
        cartRecyclerView.setLayoutManager(layoutManager);
        cartAdapter = new CartAdapter(products, product -> {
            NavController navController = Navigation.findNavController(view);
            Bundle bundle = new Bundle();
            bundle.putString("product", product);
            navController.navigate(R.id.navigateToProductDetail, bundle);
        });

        cartRecyclerView.setAdapter(cartAdapter);
        Boolean hasProducts = (products != null && !products.isEmpty());
        ctaLinearLayout.setVisibility(hasProducts ? View.VISIBLE : View.INVISIBLE);
        noContentLinearLayout.setVisibility(hasProducts ? View.INVISIBLE : View.VISIBLE);

        return view;
    }

    private void setupListenersOnView(View view) {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                Navigation.findNavController(view).popBackStack();
            }
        });

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                Navigation.findNavController(view).navigate(R.id.navigateToCheckout);
            }
        });

        continueShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                Navigation.findNavController(view).popBackStack(R.id.productsListFragment, false);
            }
        });

        noContentContinueShoppingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                Navigation.findNavController(view).popBackStack(R.id.productsListFragment, false);
            }
        });
    }
}