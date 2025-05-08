package com.example.cookey;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class IngredientSelectDialog extends Dialog {

    private OnIngredientSelectedListener listener;
    private AutoCompleteTextView autoCompleteIngredient;
    private EditText editTextQuantity;
    private Button confirmButton;

    private List<IngredientModel> allIngredients;
    private IngredientModel selectedIngredient;

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

        autoCompleteIngredient = findViewById(R.id.autoCompleteIngredient);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextQuantity.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        DBHandler dbHandler = new DBHandler(getContext());
        allIngredients = dbHandler.getAllAddIngredients();

        List<String> ingredientNames = new ArrayList<>();
        for (IngredientModel i : allIngredients) {
            ingredientNames.add(i.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, ingredientNames);
        autoCompleteIngredient.setAdapter(adapter);

        autoCompleteIngredient.setOnItemClickListener((parent, view, position, id) -> {
            String name = (String) parent.getItemAtPosition(position);
            for (IngredientModel i : allIngredients) {
                if (i.getName().equals(name)) {
                    selectedIngredient = i;
                    break;
                }
            }
        });

        confirmButton = new Button(getContext());
        confirmButton.setText("Confirm");
        confirmButton.setOnClickListener(v -> {
            if (selectedIngredient == null) {
                Toast.makeText(getContext(), "Please select an ingredient", Toast.LENGTH_SHORT).show();
                return;
            }
            String qtyText = editTextQuantity.getText().toString().trim();
            if (qtyText.isEmpty()) {
                Toast.makeText(getContext(), "Please enter a quantity", Toast.LENGTH_SHORT).show();
                return;
            }
            double quantity;
            try {
                quantity = Double.parseDouble(qtyText);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            listener.onIngredientSelected(selectedIngredient, quantity);
            dismiss();
        });

        // Add confirm button dynamically
        ((LinearLayout) findViewById(R.id.layoutRoot)).addView(confirmButton);
    }

}
