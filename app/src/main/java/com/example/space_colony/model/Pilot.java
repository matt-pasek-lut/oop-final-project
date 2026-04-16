package com.example.space_colony.model;

import com.example.space_colony.R;

/** Balanced specialization: moderate skill (5), high resilience (4), high energy (20). */
public class Pilot extends CrewMember {
    public Pilot(String name) {
        super(name, 5, 4, 20);
    }

    @Override
    public int getImageResId() { return R.drawable.ic_pilot; }

    @Override
    public String getSpecializationName() { return "Pilot"; }
}
