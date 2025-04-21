package com.example.cookey.ui.MyRecipes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.cookey.DBHandler;
import com.example.cookey.R;

public class MyRecipesFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyRecipesFragment() {
        // Required empty public constructor
    }


    public static MyRecipesFragment newInstance(String param1, String param2) {
        MyRecipesFragment fragment = new MyRecipesFragment();
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

    private DBHandler db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_recipes, container, false);
        Button dbdropper = view.findViewById(R.id.dropDB);
        Button dbexecutor = view.findViewById(R.id.executeCMD);

        db = new DBHandler(getContext());
        dbdropper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.dropDatabase();
            }
        });
        dbexecutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command =
                    "INSERT INTO Ingredient VALUES (1, 'Whole Milk', 1.0, 'liters', 7, '[\"2024-04-05\",\"2024-04-07\"]');\n" +
                    "INSERT INTO Ingredient VALUES (2, 'Large Eggs', 12, 'count', 28, '[\"2024-04-10\",\"2024-04-15\"]');\n" +
                    "INSERT INTO Ingredient VALUES (3, 'Greek Yogurt', 500, 'grams', 14, '[\"2024-04-08\"]');\n" +
                    "INSERT INTO Ingredient VALUES (4, 'Spaghetti', 500, 'grams', 365, '[]');\n" +
                    "INSERT INTO Ingredient VALUES (5, 'Yellow Onions', 3, 'count', 30, '[\"2024-04-01\"]');\n" +
                    "INSERT INTO Ingredient VALUES (6, 'Feta Cheese', 200, 'grams', 21, '[\"2024-04-12\"]');\n" +
                    "INSERT INTO Ingredient VALUES (7, 'Cheddar Cheese', 250, 'grams', 30, '[\"2024-04-05\",\"2024-04-10\"]');\n" +
                    "INSERT INTO Ingredient VALUES (8, 'IPA Beer', 6, 'bottles', 180, '[]');\n" +
                    "INSERT INTO Ingredient VALUES (9, 'Chicken Breast', 600, 'grams', 3, '[\"2024-04-03\"]');\n" +
                    "INSERT INTO Ingredient VALUES (10, 'Fresh Basil', 1, 'bunch', 5, '[\"2024-04-04\"]');\n" +
                    "INSERT INTO Ingredient VALUES (11, 'Romaine Lettuce', 1, 'head', 7, '[\"2024-04-05\",\"2024-04-08\"]');\n" +
                    "INSERT INTO Ingredient VALUES (12, 'Strawberries', 500, 'grams', 5, '[\"2024-04-06\"]');\n" +
                    "INSERT INTO Ingredient VALUES (13, 'Baking Powder', 1, 'can', 180, '[]');\n" +
                    "INSERT INTO Ingredient VALUES (14, 'Vanilla Extract', 100, 'ml', 365, '[]');\n" +
                    "INSERT INTO Ingredient VALUES (15, 'Salmon Fillet', 300, 'grams', 2, '[\"2024-04-04\"]');\n" +
                    "INSERT INTO Ingredient VALUES (16, 'Ground Beef', 450, 'grams', 3, '[\"2024-04-05\"]');\n" +
                    "INSERT INTO Ingredient VALUES (17, 'Tomato Paste', 200, 'grams', 365, '[]');\n" +
                    "INSERT INTO Ingredient VALUES (18, 'Black Beans', 400, 'grams', 1095, '[]');\n" +
                    "INSERT INTO Ingredient VALUES (19, 'Soy Sauce', 250, 'ml', 730, '[]');\n" +
                    "INSERT INTO Ingredient VALUES (20, 'Mixed Vegetables', 750, 'grams', 180, '[\"2024-04-01\"]');";
                db.executeCommand(command);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}