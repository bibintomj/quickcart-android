package com.example.quickcart;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickcart.Model.PaymentDetails;
import com.example.quickcart.Model.ShippingAddress;
import com.example.quickcart.Services.CartService;
import com.example.quickcart.Services.OrderService;

import java.util.Calendar;

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
        totalAmountTextView.setText(String.format("$%.2f", CartService.getInstance().getTotalPriceWithTax()));
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
                if (validateFields()) {
                    initiatePaymentAndPlaceOrder();
                }
            }
        });
    }

    private boolean validateFields() {
        boolean isValid = true;

        // Checking if name is empty
        if (TextUtils.isEmpty(fullNameEditText.getText())) {
            fullNameEditText.setError("Full name is required");
            isValid = false;
        }

        // checking if number is empty as well as it matches phone pattern
        if (TextUtils.isEmpty(phoneEditText.getText()) || !Patterns.PHONE.matcher(phoneEditText.getText()).matches()) {
            phoneEditText.setError("Invalid phone number");
            isValid = false;
        }

        // checking if empty
        if (TextUtils.isEmpty(houseNumberEditText.getText())) {
            houseNumberEditText.setError("House number is required");
            isValid = false;
        }

        // checking if empty
        if (TextUtils.isEmpty(streetNameEditText.getText())) {
            streetNameEditText.setError("Street name is required");
            isValid = false;
        }

        // checking if empty
        if (TextUtils.isEmpty(cityEditText.getText())) {
            cityEditText.setError("City is required");
            isValid = false;
        }

        // postal code validation.
        if (TextUtils.isEmpty(postalCodeEditText.getText())) {
            postalCodeEditText.setError("Postal code is required");
            isValid = false;
        } else {
            String postalCode = postalCodeEditText.getText().toString().trim();
            if (!postalCode.matches("^[A-Za-z]\\d[A-Za-z][ -]?\\d[A-Za-z]\\d$")) { // this matches canada's postal code pattern
                postalCodeEditText.setError("Postal code must be in format XXX XXX. (e.g., N2M 3T2)");
                isValid = false;
            }
        }

        if (TextUtils.isEmpty(nameOnCardEditText.getText())) {
            nameOnCardEditText.setError("Name on card is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(cardNumberEditText.getText()) || cardNumberEditText.getText().length() != 16) {
            cardNumberEditText.setError("Invalid card number");
            isValid = false;
        }
        if (TextUtils.isEmpty(expiryEditText.getText()) || !expiryEditText.getText().toString().matches("\\d{2}/\\d{2}")) {
            expiryEditText.setError("Invalid expiry date (MM/YY)");
            isValid = false;
        } else {
            // parsing input
            String[] parts = expiryEditText.getText().toString().split("/");
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]) + 2000; // convert YY to YYYY

            // check month range
            if (month < 1 || month > 12) {
                expiryEditText.setError("Invalid month in expiry date");
                isValid = false;
            } else {
                // checking if date is future
                Calendar calendar = Calendar.getInstance();
                int currentMonth = calendar.get(Calendar.MONTH) + 1;
                int currentYear = calendar.get(Calendar.YEAR);

                if (year < currentYear || (year == currentYear && month < currentMonth)) {
                    expiryEditText.setError("Expiry date must be in the future");
                    isValid = false;
                }
            }
        }

        if (TextUtils.isEmpty(cvvEditText.getText()) || cvvEditText.getText().length() != 3) {
            cvvEditText.setError("Invalid CVV");
            isValid = false;
        }

        return isValid;
    }

    private void initiatePaymentAndPlaceOrder() {
        CartService cartService = CartService.getInstance();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

        // Trimmed input values
        String fullName = fullNameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String houseNumber = houseNumberEditText.getText().toString().trim();
        String streetName = streetNameEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        String postalCode = postalCodeEditText.getText().toString().trim();

        // make ShippingAddress object
        ShippingAddress shippingAddress = new ShippingAddress(fullName, phone,
                houseNumber, streetName,
                city, postalCode,
                provinceSpinner.getSelectedItem().toString()
        );

        // get payment info
        String nameOnCard = nameOnCardEditText.getText().toString().trim();
        String cardNumber = cardNumberEditText.getText().toString().trim();
        String expiry = expiryEditText.getText().toString().trim();
        String cvv = cvvEditText.getText().toString().trim();

        // make PaymentDetails object
        PaymentDetails paymentDetails = new PaymentDetails(nameOnCard, cardNumber, expiry, cvv);

        OrderService.getInstance().placeOrder(
                userId,
                shippingAddress,
                paymentDetails,
                cartService.getCartItems(), // Pass the Map<Product, Integer>
                cartService.getTotalPriceWithTax(),
                new OrderService.OrderCallback() {
                    @Override
                    public void onSuccess() {
                        CartService.getInstance().clearCart();
                        Navigation.findNavController(getView()).navigate(R.id.navigateToOrderStatus);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(getContext(), "Failed to place order: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void handleViewStateWhenActionInProgrss(boolean inProgress) {
        placeOrderButton.setEnabled(!inProgress);
        placeOrderButton.setAlpha(inProgress ? 0.6F : 1);
    }
}