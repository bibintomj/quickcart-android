package com.example.quickcart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MoreFragment extends Fragment {

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
}