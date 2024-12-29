package com.example.quickcart;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button joinButton;

    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        loginButton = view.findViewById(R.id.loginButton);
        joinButton = view.findViewById(R.id.joinButton);
        mAuth = FirebaseAuth.getInstance();

        emailEditText.setText("demo@email.com");
        passwordEditText.setText("Demo#123");
        setupListeners();
        return view;
    }

    private void setupListeners() {
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.navigateToJoin);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(emailEditText.getText()).trim();
                String password = String.valueOf(passwordEditText.getText()).trim();

                if (email.equals("demo@email.com") && password.equals("Demo#123")) {
                    Navigation.findNavController(view).navigate(R.id.navigateToProductsList);
                    Toast.makeText(getContext(),  "Demo Login", Toast.LENGTH_SHORT).show();
                } else if (validateEmail(email) && validatePassword(password)) {
                    // If validation passes, perform login
                    performLogin(email, password, view);
                }
            }
        });
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            return false;
        }
        // regex check
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Invalid email format");
            return false;
        }
        return true;
    }

    private boolean validatePassword(String password) {
        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            return false;
        }
        return true;
    }

    private void performLogin(String email, String password, View view) {
        handleViewStateWhenActionInProgrss(true);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        handleViewStateWhenActionInProgrss(false);
                        if (task.isSuccessful()) {
                            // login success. store user data locally
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                storeUserData(user);
                                Navigation.findNavController(view).navigate(R.id.navigateToProductsList);
                            }
                        } else {
                            // login failed. showing message.
                            Log.w("Login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(),  task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // store user data locally in SharedPreferences.
    private void storeUserData(FirebaseUser user) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userId", user.getUid());
        editor.putString("userEmail", user.getEmail());
        editor.putString("userDisplayName", user.getDisplayName());
        editor.apply();
    }

    private void handleViewStateWhenActionInProgrss(boolean inProgress) {
        loginButton.setEnabled(!inProgress);
        loginButton.setAlpha(inProgress ? 0.6F : 1);
        joinButton.setEnabled(!inProgress);
        joinButton.setAlpha(inProgress ? 0.6F : 1);
    }
}