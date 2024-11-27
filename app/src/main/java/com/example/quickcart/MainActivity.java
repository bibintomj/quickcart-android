package com.example.quickcart;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));

        FirebaseApp.initializeApp(this);
        setupInitialFragment();
    }

    private void setupInitialFragment() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        // Check if the user is logged in
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            // User is logged in, navigate to ProductListFragment
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.splashFragment, true) // Clear the back stack
                    .build();
            navController.navigate(R.id.productsListFragment, null, navOptions);
        } else {
            // User is not logged in, remain at LoginFragment
            navController.navigate(R.id.splashFragment);
        }
    }
}