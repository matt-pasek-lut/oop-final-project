package com.example.space_colony.model;

import com.example.space_colony.R;

/** Research specialization: very high skill (8), minimal resilience (1), low energy (17). Glass cannon. */
public class Scientist extends CrewMember {
    public Scientist(String name) {
        super(name, 8, 1, 17);
    }

    @Override
    public int getImageResId() { return R.drawable.ic_scientist; }

    @Override
    public String getSpecializationName() { return "Scientist"; }
}
