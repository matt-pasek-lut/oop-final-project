package com.example.space_colony.logic;

import com.example.space_colony.model.CrewMember;
import com.example.space_colony.model.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Storage {
    private static Storage instance;
    private final HashMap<Integer, CrewMember> crew = new HashMap<>();
    private int nextId = 1;

    private Storage() {}

    public static Storage getInstance() {
        if (instance == null) instance = new Storage();
        return instance;
    }

    public int add(CrewMember member) {
        member.setId(nextId);
        crew.put(nextId, member);
        return nextId++;
    }

    public void remove(int id) {
        crew.remove(id);
    }

    public List<CrewMember> getByLocation(Location location) {
        List<CrewMember> result = new ArrayList<>();
        for (CrewMember m : crew.values()) {
            if (m.getLocation() == location) result.add(m);
        }
        return result;
    }

    public Collection<CrewMember> getAll() {
        return crew.values();
    }

    public HashMap<Integer, CrewMember> getCrew() {
        return crew;
    }

    public void clear() {
        crew.clear();
        nextId = 1;
    }

    public int getNextId() {
        return nextId;
    }

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }
}
