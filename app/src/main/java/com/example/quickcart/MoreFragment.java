package com.example.quickcart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MoreFragment extends Fragment {

    Button purchaseHistoryButton;
    TextView appInfoTextView;
    Button logoutButton;
    TextView greetingTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        initializeTabBar(view);
        purchaseHistoryButton = view.findViewById(R.id.purchaseHistoryButton);
        appInfoTextView = view.findViewById(R.id.appInfoTextView);
        logoutButton = view.findViewById(R.id.logoutButton);
        greetingTextView = view.findViewById(R.id.greetingTextView);
        String fullName = null;
        try {
            fullName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        } catch (Exception e) {
            e.printStackTrace();  // Optionally log the exception or handle it in another way
            // You can also set fullName to a default value if desired
            fullName = "Unknown";
        }
        greetingTextView.setText("Hello " + fullName.trim().split("\\s+")[0] + "! 👋");
        setupListeners();
        setAppVersionAndBuild();
        return view;
    }

    private void initializeTabBar(View view) {
        LinearLayout tabItem1 = view.findViewById(R.id.tab_item_1);
        LinearLayout tabItem2 = view.findViewById(R.id.tab_item_2);

        tabItem2.setSelected(true);
        // Tab item 1 (Home)
        tabItem1.setOnClickListener(v -> {
            Navigation.findNavController(view).popBackStack();
        });
    }

    private void setupListeners() {
        purchaseHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.navigateToOrderList);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initiateLogout();
            }
        });
    }

    private void initiateLogout() {
        // Create an AlertDialog to confirm logout
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setNegativeButton("Logout", (dialog, which) -> {
                    clearUserData();
                    FirebaseAuth.getInstance().signOut();
                    navigateBackToLogin();
                })
                .setPositiveButton("Not now", null)
                .create();

        // Change button colors
        alertDialog.setOnShowListener(dialog -> {
            Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

            // Set custom colors for the buttons
            positiveButton.setTextColor(requireContext().getColor(R.color.brandMain));
            negativeButton.setTextColor(requireContext().getColor(R.color.red));
        });

        alertDialog.show();
    }

    private void navigateBackToLogin() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.loginFragment, null, new NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment, false)
                .build());
    }

    private void clearUserData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private void setAppVersionAndBuild() {
        try {
            // Retrieve package info
            PackageManager packageManager = requireContext().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(requireContext().getPackageName(), 0);

            // Get version name and build number
            String versionName = packageInfo.versionName;
            int buildNumber = packageInfo.versionCode;

            // Set the text in the TextView
            appInfoTextView.setText("Version: " + versionName + " (Build: " + buildNumber + ")");
        } catch (PackageManager.NameNotFoundException e) {
            appInfoTextView.setText("Unable to fetch version info");
        }
    }
}