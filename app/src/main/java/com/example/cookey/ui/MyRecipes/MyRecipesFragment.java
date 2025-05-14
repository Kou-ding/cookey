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

    public MyRecipesFragment() {
        // Required empty public constructor
    }
    private DBHandler db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_recipes, container, false);

        Button dbDropper = view.findViewById(R.id.dropDB);
        Button dbExecutor = view.findViewById(R.id.executeCMD);

        db = new DBHandler(getContext(),null,null,1);
        dbDropper.setOnClickListener(v -> db.dropDatabase());
        dbExecutor.setOnClickListener(v -> {
            String command =
                    // Add Shopping List Items
                    // Food
                    "INSERT INTO ShoppingList VALUES (1, 'Milk', 1.0, 1, 0);\n" +
                    "INSERT INTO ShoppingList VALUES (2, 'Eggs', 12, 1, 0);\n" +
                    "INSERT INTO ShoppingList VALUES (3, 'Yogurt', 500, 1, 0);\n" +
                    "INSERT INTO ShoppingList VALUES (4, 'Spaghetti', 500, 1, 0);\n" +
                    "INSERT INTO ShoppingList VALUES (5, 'Onions', 3, 1, 0);\n" +
                    "INSERT INTO ShoppingList VALUES (6, 'Feta Cheese', 200, 1, 0);\n" +
                    "INSERT INTO ShoppingList VALUES (7, 'Cheddar Cheese', 250, 1, 0);\n" +
                    "INSERT INTO ShoppingList VALUES (8, 'Beer', 6, 1, 0);\n" +
                    "INSERT INTO ShoppingList VALUES (9, 'Chicken Breast', 600, 1, 0);\n" +
                    "INSERT INTO ShoppingList VALUES (10, 'Basil', 1, 1, 0);\n" +

                    // Non-Food
                    "INSERT INTO ShoppingList VALUES (11, 'Toilet Paper', 4.0, 0, 0);\n" +
                    "INSERT INTO ShoppingList VALUES (12, 'Soap', 2.0, 0, 0);\n" +
                    "INSERT INTO ShoppingList VALUES (13, 'Shampoo', 1.0, 0, 0);\n" +
                    "INSERT INTO ShoppingList VALUES (14, 'Toothpaste', 1.0, 0, 0);\n";
            db.executeCommand(command);
        });

        // Inflate the layout for this fragment
        return view;
    }
}