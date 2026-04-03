package com.example.space_colony.model;

import com.example.space_colony.R;

public class Medic extends CrewMember {
    public Medic(String name) {
        super(name, 7, 2, 18);
    }

    @Override
    public int getImageResId() { return R.drawable.ic_medic; }

    @Override
    public String getSpecializationName() { return "Medic"; }
}
