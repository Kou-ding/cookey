package com.example.cookey;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryVH> {

    public interface OnCountryClickListener {
        void onCountryClick(CountryModel c);
    }

    private final Context ctx;
    private final List<CountryModel> data;
    private final OnCountryClickListener listener;

    public CountryAdapter(Context ctx, List<CountryModel> data,
                          OnCountryClickListener listener) {
        this.ctx = ctx;
        this.data = data;
        this.listener = listener;
    }

    // ---------- view holder ----------
    static class CountryVH extends RecyclerView.ViewHolder {
        final ImageView flag;
        final TextView name;

        CountryVH(@NonNull View v) {
            super(v);
            flag = v.findViewById(R.id.imageViewFlag);
            name = v.findViewById(R.id.textViewCountryName);
        }
    }

    @NonNull
    @Override
    public CountryVH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        View row = LayoutInflater.from(p.getContext())
                .inflate(R.layout.item_country, p, false);
        return new CountryVH(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryVH h, int pos) {
        CountryModel m = data.get(pos);
        h.name.setText(m.getName());

        // load flag from assets/ or show placeholder
        Drawable d = loadFlag(ctx, m.getFlagAssetPath());
        h.flag.setImageDrawable(d);

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onCountryClick(m);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private Drawable loadFlag(Context c, String relPath) {
        if (relPath == null) { //flag not found
            return ContextCompat.getDrawable(c, R.drawable.ic_flag_placeholder);
        }
        try (InputStream is = c.getAssets().open(relPath)) {
            return Drawable.createFromStream(is, relPath);
        } catch (IOException e) {
            return ContextCompat.getDrawable(c, R.drawable.ic_flag_placeholder);
        }
    }
}
