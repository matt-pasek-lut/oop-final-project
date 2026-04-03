package com.example.space_colony.model;

import com.example.space_colony.R;

public class Engineer extends CrewMember {
    public Engineer(String name) {
        super(name, 6, 3, 19);
    }

    @Override
    public int getImageResId() { return R.drawable.ic_engineer; }

    @Override
    public String getSpecializationName() { return "Engineer"; }
}
