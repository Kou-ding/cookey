package com.example.cookey.ui.MyShoppingList;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.cookey.DBHandler;
import com.example.cookey.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyShoppingListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyShoppingListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyShoppingListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyShoppingListFragment newInstance(String param1, String param2) {
        MyShoppingListFragment fragment = new MyShoppingListFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_shopping_list, container, false);

        // ✅ THIS is the correct way to reference your LinearLayout directly
        LinearLayout listLayout = view.findViewById(R.id.shoppingListLayout);

        Button addFoodButton = view.findViewById(R.id.addFood);
        Button addNonFoodButton = view.findViewById(R.id.addNonFood);

        addFoodButton.setOnClickListener(v -> {
            addAutoCompleteTextView(listLayout);
        });

        return view;
    }
    private void showIngredientInput(LinearLayout parentLayout) {
        EditText ingredientInput = new EditText(getContext());
        ingredientInput.setHint("Type ingredient...");
        ingredientInput.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        // Set margin (optional)
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ingredientInput.getLayoutParams();
        params.topMargin = dpToPx(8);
        params.bottomMargin = dpToPx(8);
        ingredientInput.setLayoutParams(params);

        // Find the index of the Add button
        int addButtonIndex = getIndexOfChildById(parentLayout, R.id.addFood);

        // Insert the EditText just before the Add button
        parentLayout.addView(ingredientInput, addButtonIndex);
    }

    private int getIndexOfChildById(LinearLayout parent, int id) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (parent.getChildAt(i).getId() == id) {
                return i;
            }
        }
        return parent.getChildCount(); // fallback: end of layout
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void addAutoCompleteTextView(LinearLayout parentLayout) {
        AutoCompleteTextView autoCompleteTextView = new AutoCompleteTextView(getContext());
        autoCompleteTextView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        autoCompleteTextView.setHint("Type ingredient...");
        autoCompleteTextView.setTextSize(16);
        autoCompleteTextView.setThreshold(1); // Start suggesting from first character

        // Set up text change listener to update suggestions
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSuggestions(autoCompleteTextView, s.toString());
            }
        });

        // Add it to layout at correct position (below "Food", above Add button)
        int addButtonIndex = getButtonIndex(parentLayout, R.id.addFood);
        parentLayout.addView(autoCompleteTextView, addButtonIndex);
    }
    private void updateSuggestions(AutoCompleteTextView view, String query) {
        DBHandler dbHandler = new DBHandler(getContext());
        String[] matches = dbHandler.similarIngredients(query);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_dropdown_item_1line,
                matches
        );
        view.setAdapter(adapter);
        view.showDropDown(); // Optional: always show dropdown as you type
    }
    private int getButtonIndex(LinearLayout layout, int buttonId) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child.getId() == buttonId) {
                return i;
            }
        }
        return layout.getChildCount(); // fallback
    }







}