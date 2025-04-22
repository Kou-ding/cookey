package com.example.cookey.ui.MyIngredients;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cookey.Ingredient;
import com.example.cookey.IngredientAdapter;
import com.example.cookey.R;

import java.util.ArrayList;

public class MyIngredientsFragment extends Fragment {

    private IngredientAdapter adapter;
    private MyIngredientsViewModel viewModel;
    private boolean editing = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_ingredients, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.myIngredientRecyclerView);
        Button editButton = view.findViewById(R.id.editButton);

        viewModel = new ViewModelProvider(this).get(MyIngredientsViewModel.class);
        viewModel.loadIngredients(requireContext());

        adapter = new IngredientAdapter(new ArrayList<>(), requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        viewModel.getIngredients().observe(getViewLifecycleOwner(), ingredients -> {
            adapter.updateIngredients(ingredients);
            editButton.setEnabled(!ingredients.isEmpty());
        });

        editButton.setOnClickListener(v -> {
            editing = !editing;
            adapter.setEditingEnabled(editing);
            editButton.setText(editing ? "Done" : "Edit");
        });

        adapter.setOnDeleteClickListener(ingredient -> {
            viewModel.deleteIngredient(requireContext(), ingredient.getIngredientId());
        });

        return view;
    }
}
