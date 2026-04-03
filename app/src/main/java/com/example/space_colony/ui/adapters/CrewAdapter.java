package com.example.space_colony.ui.adapters;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.space_colony.R;
import com.example.space_colony.model.CrewMember;

import java.util.ArrayList;
import java.util.List;

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.ViewHolder> {
    private final List<CrewMember> crew = new ArrayList<>();
    private final SparseBooleanArray checked = new SparseBooleanArray();
    private int maxSelection = Integer.MAX_VALUE;

    public CrewAdapter(List<CrewMember> initialCrew) {
        crew.addAll(initialCrew);
    }

    public void setMaxSelection(int max) {
        maxSelection = max;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_crew, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CrewMember m = crew.get(position);
        holder.name.setText(m.getSpecializationName() + ": " + m.getName());
        holder.stats.setText("Skill " + m.getSkill() + "  Res " + m.getResilience()
            + "  HP " + m.getEnergy() + "/" + m.getMaxEnergy()
            + "  XP " + m.getExperience());
        holder.image.setImageResource(m.getImageResId());
        holder.checkbox.setOnCheckedChangeListener(null);
        holder.checkbox.setChecked(checked.get(position, false));
        holder.checkbox.setOnCheckedChangeListener((btn, isChecked) -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_ID) return;
            if (isChecked && getCheckedCount() >= maxSelection) {
                btn.setChecked(false);
                return;
            }
            checked.put(pos, isChecked);
        });
    }

    private int getCheckedCount() {
        int count = 0;
        for (int i = 0; i < crew.size(); i++) {
            if (checked.get(i, false)) count++;
        }
        return count;
    }

    public List<CrewMember> getChecked() {
        List<CrewMember> result = new ArrayList<>();
        for (int i = 0; i < crew.size(); i++) {
            if (checked.get(i, false)) result.add(crew.get(i));
        }
        return result;
    }

    public void refresh(List<CrewMember> newCrew) {
        crew.clear();
        crew.addAll(newCrew);
        checked.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return crew.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;
        final TextView name, stats;
        final CheckBox checkbox;

        ViewHolder(@NonNull View v) {
            super(v);
            image = v.findViewById(R.id.crew_image);
            name = v.findViewById(R.id.crew_name);
            stats = v.findViewById(R.id.crew_stats);
            checkbox = v.findViewById(R.id.checkbox);
        }
    }
}
