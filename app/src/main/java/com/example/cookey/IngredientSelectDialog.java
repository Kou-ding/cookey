package com.example.cookey;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class IngredientSelectDialog extends Dialog {

    private RecyclerView recyclerView;
    private IngredientAdapter adapter;
    private OnIngredientSelectedListener listener;
    private AutoCompleteTextView autoComplete;

    public interface OnIngredientSelectedListener {
        void onIngredientSelected(IngredientModel ingredient, double quantity);
    }

    public IngredientSelectDialog(@NonNull Context context, OnIngredientSelectedListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_ingredient);

        autoComplete = findViewById(R.id.autoCompleteIngredient);
        recyclerView = findViewById(R.id.recyclerViewIngredients);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DBHandler dbHandler = new DBHandler(getContext());
        List<IngredientModel> ingredients = dbHandler.getAllAddIngredients();

        adapter = new IngredientAdapter(ingredients, ingredient -> showQuantityDialog(ingredient));

        adapter = new IngredientAdapter(ingredients, ingredient -> showQuantityDialog(ingredient));
        recyclerView.setAdapter(adapter);

        List<String> ingredientNames = new ArrayList<>();
        for (IngredientModel ing : ingredients) {
            ingredientNames.add(ing.getName());
        }

        ArrayAdapter<String> autoAdapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_dropdown_item_1line, ingredientNames);
        autoComplete.setAdapter(autoAdapter);

        autoComplete.setOnItemClickListener((parent, view, position, id) -> {
            String selectedName = (String) parent.getItemAtPosition(position);
            for (IngredientModel ing : ingredients) {
                if (ing.getName().equalsIgnoreCase(selectedName)) {
                    showQuantityDialog(ing);
                    break;
                }
            }
        });
    }

    private void showQuantityDialog(IngredientModel ingredient) {
        Dialog quantityDialog = new Dialog(getContext());
        quantityDialog.setContentView(R.layout.dialog_enter_quantity);
        EditText editTextQuantity = quantityDialog.findViewById(R.id.editTextQuantity);
        quantityDialog.findViewById(R.id.buttonConfirmQuantity).setOnClickListener(v -> {
            String quantityText = editTextQuantity.getText().toString().trim();
            if (!quantityText.isEmpty()) {
                double quantity = Double.parseDouble(quantityText);
                listener.onIngredientSelected(ingredient, quantity);
                quantityDialog.dismiss();
                dismiss();
            } else {
                Toast.makeText(getContext(), "Please enter a quantity", Toast.LENGTH_SHORT).show();
            }
        });
        quantityDialog.show();
    }
}
