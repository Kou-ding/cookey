package com.example.cookey;

import android.nfc.Tag;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {

    private List<String> tags;
    private Set<String> selectedTags = new HashSet<>();

    public TagAdapter(List<String> tags){
        this.tags = tags;
    }

    public Set<String> getSelectedTags(){
        return selectedTags;
    }

    @NotNull
    @Override
    public TagViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        String tag = tags.get(position);
        holder.checkBox.setText(tag);

        //Weird tag bug - fix?
        holder.checkBox.setOnCheckedChangeListener(null);

        holder.checkBox.setChecked(selectedTags.contains(tag));

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedTags.add(tag);
            } else {
                selectedTags.remove(tag);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    static class TagViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBoxTag);
        }
    }

    public void setSelectedTags(List<String> tagsToSelect) {
        selectedTags.clear();
        if (tagsToSelect != null) {
            selectedTags.addAll(tagsToSelect);
        }
        notifyDataSetChanged();
    }
}
