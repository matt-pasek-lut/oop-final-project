package com.example.space_colony.logic;

import com.example.space_colony.model.CrewMember;
import com.example.space_colony.model.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Singleton in-memory registry that holds all active crew members.
 * <p>
 * Uses a {@code HashMap<Integer, CrewMember>} for O(1) lookup by ID.
 * All UI fragments read and modify crew state exclusively through this class.
 * Persistence is handled separately by {@link com.example.space_colony.utils.FileManager}.
 */
public class Storage {
    private static Storage instance;
    private final HashMap<Integer, CrewMember> crew = new HashMap<>();
    private int nextId = 1;

    private Storage() {}

    /** Returns the application-wide singleton instance, creating it on first call. */
    public static Storage getInstance() {
        if (instance == null) instance = new Storage();
        return instance;
    }

    /**
     * Assigns a unique ID to {@code member} and stores it.
     *
     * @param member the newly recruited crew member to register
     * @return the assigned ID
     */
    public int add(CrewMember member) {
        member.setId(nextId);
        crew.put(nextId, member);
        return nextId++;
    }

    /**
     * Permanently removes a crew member from the roster.
     * Called when a member's energy drops to zero during a mission.
     *
     * @param id the ID of the crew member to remove
     */
    public void remove(int id) {
        crew.remove(id);
    }

    /**
     * Returns all crew members currently at the given location.
     * Used by each fragment's RecyclerView to show only the relevant subset.
     *
     * @param location the location filter (QUARTERS, SIMULATOR, or MISSION_CONTROL)
     * @return a new list of matching crew members
     */
    public List<CrewMember> getByLocation(Location location) {
        List<CrewMember> result = new ArrayList<>();
        for (CrewMember m : crew.values()) {
            if (m.getLocation() == location) result.add(m);
        }
        return result;
    }

    /** @return an unordered view of all crew members regardless of location */
    public Collection<CrewMember> getAll() {
        return crew.values();
    }

    /** @return the underlying map, used by FileManager when saving/loading crew state */
    public HashMap<Integer, CrewMember> getCrew() {
        return crew;
    }

    /** Removes all crew members and resets the ID counter. Called before loading a save file. */
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
