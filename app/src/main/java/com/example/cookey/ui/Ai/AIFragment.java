package com.example.cookey.ui.Ai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.example.cookey.AIRecipeEditActivity;
import com.example.cookey.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class AIFragment extends Fragment {

    private boolean useIngredientsMode = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout just once
        View view = inflater.inflate(R.layout.fragment_ai, container, false);

        // Cook Button //
        Button cookButton = view.findViewById(R.id.generate_ai_recipe);
        cookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inside your AIFragment, replace navigation with:
                Intent intent = new Intent(requireActivity(), AIRecipeEditActivity.class);
                startActivity(intent);
            }
        });

        // Expand Button //
        // Initialize views from the layout
        MaterialButton expandButton = view.findViewById(R.id.expandButton);

        // Set click listener for the expand button
        expandButton.setOnClickListener(v -> {
            // Toggle the useIngredientsMode flag
            useIngredientsMode = !useIngredientsMode;

            // Change the button icon
            Drawable newIcon = ContextCompat.getDrawable(
                    requireContext(),
                    useIngredientsMode
                            ? R.drawable.ic_keyboard_arrow_up // Down arrow when collapsing
                            : R.drawable.ic_keyboard_arrow_down     // Up arrow when expanding
            );
            expandButton.setIcon(newIcon);


        });

        return view;
    }
}
