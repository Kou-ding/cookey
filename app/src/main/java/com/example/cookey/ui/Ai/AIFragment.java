package com.example.cookey.ui.Ai;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.cookey.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AIFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AIFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LinearLayout checkboxContainer;
    private final List<CheckBox> checkboxes = new ArrayList<>();
    public AIFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AIFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AIFragment newInstance(String param1, String param2) {
        AIFragment fragment = new AIFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_AIFragment_to_AIRecipeEditFragment);
            }
        });

        // Expand Button //
        // Initialize views from the layout
        Button expandButton = view.findViewById(R.id.expandButton);
        checkboxContainer = view.findViewById(R.id.checkboxContainer);

        // Set click listener for the expand button
        expandButton.setOnClickListener(v -> {
            boolean isVisible = checkboxContainer.getVisibility() == View.VISIBLE;
            checkboxContainer.setVisibility(isVisible ? View.GONE : View.VISIBLE);

            // Change down arrow (optional)
            // Change the button icon
            Drawable newIcon = ContextCompat.getDrawable(
                    requireContext(),
                    isVisible
                            ? R.drawable.keyboard_arrow_down_24dp_e3e3e3_fill0_wght400_grad0_opsz24  // Down arrow when collapsing
                            : R.drawable.keyboard_arrow_up_24dp_e3e3e3_fill0_wght400_grad0_opsz24    // Up arrow when expanding
            );
            ((MaterialButton) expandButton).setIcon(newIcon);

            // Add first checkbox only when first expanded
            if (!isVisible && checkboxes.isEmpty()) {
                addCheckbox();
            }
        });

        // Add initial checkbox (and subsequent ones dynamically)
        addCheckbox();

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
        textInputLayout.setStartIconDrawable(ContextCompat.getDrawable(context, R.drawable.check_box_outline_blank_24dp_e3e3e3_fill0_wght400_grad0_opsz24)); // Initial unchecked icon
        textInputLayout.setStartIconOnClickListener(v -> {
            checkBox.setChecked(!checkBox.isChecked());
            textInputLayout.setStartIconDrawable(ContextCompat.getDrawable(context, checkBox.isChecked() ? R.drawable.select_check_box_24dp_e3e3e3_fill0_wght400_grad0_opsz24 : R.drawable.check_box_outline_blank_24dp_e3e3e3_fill0_wght400_grad0_opsz24)); // Update icon
            //Optional: If you want to trigger the editText focus on checkbox click
            if (checkBox.isChecked()) {
                editText.requestFocus(); // Request focus when checked
            }
        });

        // Add to checkbox container
        checkboxContainer.addView(textInputLayout);

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

}
