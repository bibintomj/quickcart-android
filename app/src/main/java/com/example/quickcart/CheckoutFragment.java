package com.example.quickcart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class CheckoutFragment extends Fragment {

    // Variable declarations
    private ImageButton backButton;
    private TextView titleTextView;

    private EditText fullNameEditText;
    private EditText phoneEditText;
    private EditText houseNumberEditText;
    private EditText streetNameEditText;
    private EditText cityEditText;
    private EditText postalCodeEditText;
    private Spinner provinceSpinner;

    private EditText nameOnCardEditText;
    private EditText cardNumberEditText;
    private EditText expiryEditText;
    private EditText cvvEditText;

    private TextView totalAmountTextView;
    private Button placeOrderButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        backButton = view.findViewById(R.id.backButton);
        titleTextView = view.findViewById(R.id.titleTextView);

        fullNameEditText = view.findViewById(R.id.fullNameEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        houseNumberEditText = view.findViewById(R.id.houseNumberEditText);
        streetNameEditText = view.findViewById(R.id.streetNameEditText);
        cityEditText = view.findViewById(R.id.cityEditText);
        postalCodeEditText = view.findViewById(R.id.postalCodeEditText);
        provinceSpinner = view.findViewById(R.id.provinceSpinner);

        nameOnCardEditText = view.findViewById(R.id.nameOnCardEditText);
        cardNumberEditText = view.findViewById(R.id.cardNumberEditText);
        expiryEditText = view.findViewById(R.id.expiryEditText);
        cvvEditText = view.findViewById(R.id.cvvEditText);

        totalAmountTextView = view.findViewById(R.id.totalAmountTextView);
        placeOrderButton = view.findViewById(R.id.placeOrderButton);
        setupListenersOnView(view);
        return view;
    }

    public void setupListenersOnView(View view) {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                Navigation.findNavController(view).popBackStack();
            }
        });

        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                Navigation.findNavController(view).navigate(R.id.navigateToOrderStatus);
            }
        });
    }
}