package com.example.space_colony.model;

import com.example.space_colony.R;

/** Combat specialization: maximum skill (9), zero resilience, lowest energy (16). Pure damage dealer. */
public class Soldier extends CrewMember {
    public Soldier(String name) {
        super(name, 9, 0, 16);
    }

    @Override
    public int getImageResId() { return R.drawable.ic_soldier; }

    @Override
    public String getSpecializationName() { return "Soldier"; }
}
