package com.example.quickcart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class OrderStatusFragment extends Fragment {

    private ImageView checkmarkImage;
    private Button goHomeButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_status, container, false);
        checkmarkImage = view.findViewById(R.id.checkmarkImageView);
        goHomeButton = view.findViewById(R.id.goHomeButton);
        goHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack(R.id.productsListFragment, false);
            }
        });
        animateCheckmark();
        return view;
    }

    private void animateCheckmark() {
        Animation scaleAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.checkmark_scale);
        checkmarkImage.setVisibility(View.VISIBLE);
        checkmarkImage.startAnimation(scaleAnimation);
    }
}