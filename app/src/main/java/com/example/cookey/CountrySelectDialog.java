package com.example.cookey;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CountrySelectDialog extends Dialog {

    private RecyclerView recyclerView;
    private CountryAdapter adapter;
    private OnCountrySelectedListener listener;

    public interface OnCountrySelectedListener {
        void onCountrySelected(CountryModel country);
    }

    public CountrySelectDialog(@NonNull Context context, OnCountrySelectedListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_country);

        recyclerView = findViewById(R.id.recyclerViewCountries);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<CountryModel> dummyCountries = new ArrayList<>();
        dummyCountries.add(new CountryModel(1, "Korea", R.drawable.kr));
        dummyCountries.add(new CountryModel(2, "Japan", R.drawable.jp));
        dummyCountries.add(new CountryModel(3, "Germany", R.drawable.de));
        dummyCountries.add(new CountryModel(4, "Italy", R.drawable.it));
        dummyCountries.add(new CountryModel(5, "Greece", R.drawable.gr));

        adapter = new CountryAdapter(dummyCountries, country -> {
            if (listener != null) {
                listener.onCountrySelected(country);
                dismiss();
            }
        });

        recyclerView.setAdapter(adapter);
    }
}
