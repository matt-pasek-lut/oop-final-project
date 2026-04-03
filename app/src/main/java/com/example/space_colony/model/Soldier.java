package com.example.space_colony.model;

import com.example.space_colony.R;

public class Soldier extends CrewMember {
    public Soldier(String name) {
        super(name, 9, 0, 16);
    }

    @Override
    public int getImageResId() { return R.drawable.ic_soldier; }

    @Override
    public String getSpecializationName() { return "Soldier"; }
}
