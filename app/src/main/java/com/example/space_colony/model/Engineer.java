package com.example.space_colony.model;

import com.example.space_colony.R;

/** Technical specialization: above-average skill (6), good resilience (3), high energy (19). */
public class Engineer extends CrewMember {
    public Engineer(String name) {
        super(name, 6, 3, 19);
    }

    @Override
    public int getImageResId() { return R.drawable.ic_engineer; }

    @Override
    public String getSpecializationName() { return "Engineer"; }
}
