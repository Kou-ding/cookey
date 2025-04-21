package com.example.cookey.ui.MyIngredients;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.cookey.DBHandler;
import com.example.cookey.Ingredient;
import com.example.cookey.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyIngredientsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyIngredientsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyIngredientsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyIngredientsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyIngredientsFragment newInstance(String param1, String param2) {
        MyIngredientsFragment fragment = new MyIngredientsFragment();
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

    private ListView ingredientListView;
    private DBHandler dbHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_ingredients, container, false); // Replace your_layout_file
        ingredientListView = view.findViewById(R.id.myIngredientListView); // Replace with your ListView's ID
        dbHandler = new DBHandler(requireContext()); // Initialize your DBHandler
        populateListView();
        return view;
    }
    private void populateListView() {
        Ingredient[] ingredients = dbHandler.getAllIngredients();
        if (ingredients != null && ingredients.length > 0) {
            List<String> ingredientNames = new ArrayList<>();
            for (Ingredient ingredient : ingredients) {
                ingredientNames.add(ingredient.getIngredientName()); // Or any other relevant data
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    requireContext(),
                    android.R.layout.simple_list_item_1, // Standard list item layout
                    ingredientNames
            );
            ingredientListView.setAdapter(adapter);
        } else {
            // Handle the case where no ingredients are found (e.g., show a message)
            // ingredientListView.setEmptyView(findViewById(R.id.empty_view));
        }
    }
}