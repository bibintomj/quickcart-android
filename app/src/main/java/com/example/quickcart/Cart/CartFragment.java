package com.example.quickcart.Cart;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.quickcart.R;
import com.example.quickcart.Services.CartService;
import com.example.quickcart.Model.Product;

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

    private LinearLayout ctaContainerLinearLayout;
    private TextView totalAmountTextView;
    private Button continueShoppingButton;
    private Button checkoutButton;

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private GridLayoutManager layoutManager;
    private List<Product> cartProducts = new ArrayList<>();

    private CartService cartService = CartService.getInstance();

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
        cartProducts = cartService.getCartProducts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        backButton = view.findViewById(R.id.backButton);
        continueShoppingButton = view.findViewById(R.id.continueShoppingButton);
        titleTextView = view.findViewById(R.id.titleTextView);

        ctaContainerLinearLayout = view.findViewById(R.id.ctaContainerLinearLayout);
        totalAmountTextView = view.findViewById(R.id.totalAmountTextView);
        checkoutButton = view.findViewById(R.id.checkoutButton);
        noContentContinueShoppingButton = view.findViewById(R.id.noContentPlaceOrderButton);
        noContentLinearLayout = view.findViewById(R.id.noContentLinearLayout);

        cartRecyclerView = view.findViewById(R.id.cartRecyclerView);
        setupListenersOnView(view);

        // Layout manager based on orientation
        int orientation = getResources().getConfiguration().orientation;
        layoutManager = new GridLayoutManager(requireContext(), orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 1);
        cartRecyclerView.setLayoutManager(layoutManager);

        // Setup cart adapter
        cartAdapter = new CartAdapter(cartProducts, product -> {
            NavController navController = Navigation.findNavController(view);
            Bundle bundle = new Bundle();
            bundle.putParcelable("product", product);
            navController.navigate(R.id.navigateToProductDetail, bundle);
        }, this::onCartChange);

        cartRecyclerView.setAdapter(cartAdapter);
        onCartChange();
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

    // Update UI visibility based on cart contents
    public void onCartChange() {
        cartProducts = cartService.getCartProducts();
        boolean hasProducts = (cartProducts != null && !cartProducts.isEmpty());
        cartRecyclerView.setVisibility(hasProducts ? View.VISIBLE : View.INVISIBLE);
        ctaContainerLinearLayout.setVisibility(hasProducts ? View.VISIBLE : View.INVISIBLE);
        noContentLinearLayout.setVisibility(hasProducts ? View.INVISIBLE : View.VISIBLE);
        continueShoppingButton.setVisibility(hasProducts ? View.VISIBLE : View.INVISIBLE);
        totalAmountTextView.setText(String.format("$%.2f", cartService.getTotalPriceWithTax()));
        cartAdapter.updateProductList(cartProducts);
    }
}