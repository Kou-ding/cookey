package com.example.cookey;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {

    private List<CountryModel> countryList;
    private OnCountryClickListener listener;

    public interface OnCountryClickListener {
        void onCountryClick(CountryModel country);
    }

    public CountryAdapter(List<CountryModel> countryList, OnCountryClickListener listener) {
        this.countryList = countryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        CountryModel country = countryList.get(position);
        holder.textViewName.setText(country.getName());
        holder.imageViewFlag.setImageResource(country.getFlagResource());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCountryClick(country);
            }
        });
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    static class CountryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewFlag;
        TextView textViewName;

        CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFlag = itemView.findViewById(R.id.imageViewFlag);
            textViewName = itemView.findViewById(R.id.textViewCountryName);
        }
    }
}
