package com.example.space_colony.model;

import com.example.space_colony.R;

public class Scientist extends CrewMember {
    public Scientist(String name) {
        super(name, 8, 1, 17);
    }

    @Override
    public int getImageResId() { return R.drawable.ic_scientist; }

    @Override
    public String getSpecializationName() { return "Scientist"; }
}
