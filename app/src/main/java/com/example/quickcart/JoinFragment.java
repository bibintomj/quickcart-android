package com.example.quickcart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class JoinFragment extends Fragment {

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private CheckBox termsCheckBox;
    private Button joinButton;
    private Button loginButton;

    private FirebaseAuth mAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_join, container, false);
        nameEditText = view.findViewById(R.id.nameEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditText);
        termsCheckBox = view.findViewById(R.id.termsCheckBox);
        joinButton = view.findViewById(R.id.joinButton);
        loginButton = view.findViewById(R.id.loginButton);
        setupListeners();
        mAuth = FirebaseAuth.getInstance();
        return view;
    }

    private void performSignUp(String name, String email, String password) {
        handleViewStateWhenActionInProgrss(true);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        handleViewStateWhenActionInProgrss(false);
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            Toast.makeText(getContext(), "Registered", Toast.LENGTH_SHORT).show();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("QC", "User profile updated.");
                                            } else {
                                                Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                            if (getView() != null) {
                                                // go to login after signup success
                                                Navigation.findNavController(requireView()).popBackStack();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setupListeners() {
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = String.valueOf(nameEditText.getText()).trim();
                String email = String.valueOf(emailEditText.getText()).trim();
                String password = String.valueOf(passwordEditText.getText()).trim();
                String confirmPassword = String.valueOf(confirmPasswordEditText.getText()).trim();
                Boolean termsAccepted = termsCheckBox.isChecked();

                // sanitize the user inputs
                if (validateInputs(name, email, password, confirmPassword, termsAccepted)) {
                    // If validation passes, perform signUp
                    performSignUp(name, email, password);
                }
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(requireView()).popBackStack();
            }
        });
    }

    private boolean validateInputs(String name, String email, String password, String confirmPassword, boolean termsAccepted) {
        // name vaildation
        if (name.isEmpty()) {
            nameEditText.setError("Name is required");
            return false;
        }

        // email validation. empty and regex matching
        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Invalid email format");
            return false;
        }

        // password validation.
        // uses 5 criteria. 8 char long, 1 uppercase, 1 lowercase, 1 number, 1 special char
        if (password.length() < 8) {
            passwordEditText.setError("Password must be at least 8 characters long");
            return false;
        }
        if (!password.matches(".*[A-Z].*")) { // Check for uppercase letter
            passwordEditText.setError("Password must contain at least one uppercase letter");
            return false;
        }
        if (!password.matches(".*[a-z].*")) { // Check for lowercase letter
            passwordEditText.setError("Password must contain at least one lowercase letter");
            return false;
        }
        if (!password.matches(".*[0-9].*")) { // Check for number
            passwordEditText.setError("Password must contain at least one number");
            return false;
        }
        if (!password.matches(".*[!@#\\$%^&*(),.?\":{}|<>].*")) { // Check for special character
            passwordEditText.setError("Password must contain at least one special character");
            return false;
        }

        // confirm password validation
        if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError("Confirm password is required");
            return false;
        }

        // password matching validation
        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            return false;
        }

        // check if terms accepted
        if (!termsAccepted) {
            Toast.makeText(getContext(), "You must accept the terms to proceed.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void handleViewStateWhenActionInProgrss(boolean inProgress) {
        joinButton.setEnabled(!inProgress);
        joinButton.setAlpha(inProgress ? 0.6F : 1);
        loginButton.setEnabled(!inProgress);
        loginButton.setAlpha(inProgress ? 0.6F : 1);
    }
}