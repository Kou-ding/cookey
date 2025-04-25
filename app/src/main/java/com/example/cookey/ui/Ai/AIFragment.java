package com.example.cookey.ui.Ai;

import android.content.Context;
import android.content.Intent;
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
    private final List<CheckBox> checkboxes = new ArrayList<>();
    public AIFragment() {
        // Required empty public constructor
    }

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
        LinearLayout checkboxContainer = view.findViewById(R.id.checkboxContainer);


        // Set click listener for the expand button
        expandButton.setOnClickListener(v -> {
            boolean isVisible = checkboxContainer.getVisibility() == View.VISIBLE;
            checkboxContainer.setVisibility(isVisible ? View.GONE : View.VISIBLE);

            // Change down arrow (optional)
            // Change the button icon
            Drawable newIcon = ContextCompat.getDrawable(
                    requireContext(),
                    isVisible
                            ? R.drawable.ic_keyboard_arrow_down  // Down arrow when collapsing
                            : R.drawable.ic_keyboard_arrow_up    // Up arrow when expanding
            );
            ((MaterialButton) expandButton).setIcon(newIcon);


        });

        // Add initial checkbox (and subsequent ones dynamically)
        if(checkboxContainer.getVisibility() == View.VISIBLE) {
            addCheckbox();
        }

        return view;
    }
    private void addCheckbox() {
        final Context context = requireContext();

        // Create TextInputLayout
        final TextInputLayout textInputLayout = new TextInputLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 8, 0, 8); // Add some spacing
        textInputLayout.setLayoutParams(layoutParams);
        textInputLayout.setHint("New ingredient"); // Initial hint

        // Create TextInputEditText
        final TextInputEditText editText = new TextInputEditText(context);
        textInputLayout.addView(editText);

        // Create MaterialCheckBox
        final MaterialCheckBox checkBox = new MaterialCheckBox(context);
        checkBox.setChecked(false);

        // Set CheckBox click listener to update input field focus
        checkBox.setOnClickListener(v -> {
            if (checkBox.isChecked()) {
                editText.requestFocus(); // Request focus when checked
            }
        });

        // Set CheckBox as start icon using built-in drawables
        textInputLayout.setStartIconDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_box_outline_blank)); // Initial unchecked icon
        textInputLayout.setStartIconOnClickListener(v -> {
            checkBox.setChecked(!checkBox.isChecked());
            textInputLayout.setStartIconDrawable(ContextCompat.getDrawable(context, checkBox.isChecked() ? R.drawable.ic_select_check_box : R.drawable.ic_check_box_outline_blank)); // Update icon
            //Optional: If you want to trigger the editText focus on checkbox click
            if (checkBox.isChecked()) {
                editText.requestFocus(); // Request focus when checked
            }
        });

        //Add checkbox to list - we'll use MaterialCheckBox instead for our checkbox list.
        checkboxes.add(checkBox);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Add new checkbox if this is the last one and input is not empty.
                if (s != null && !s.toString().trim().isEmpty() &&
                        !checkboxes.isEmpty() && checkboxes.get(checkboxes.size() - 1) == checkBox) {
                    addCheckbox(); // Add a new checkbox when text is entered in the last one
                }
            }
        });
    }

//    public String generateAIRecipe(String prompt, List<String> ingredients) {
//
//    }

}
