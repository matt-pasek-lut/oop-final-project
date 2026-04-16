package com.example.space_colony.model;

/**
 * Represents a system-generated enemy that two crew members face cooperatively in a mission.
 * <p>
 * Threats are created by {@link com.example.space_colony.logic.MissionControl#runMission}
 * with stats that scale with the number of completed missions, making each successive
 * encounter progressively harder.
 */
public class Threat {
    private final String name;
    private final int skill;
    private final int resilience;
    private int energy;
    private final int maxEnergy;

    /**
     * Creates a new threat with the given combat stats.
     *
     * @param name       display name shown in the mission log
     * @param skill      fixed attack value returned by {@link #act()}
     * @param resilience damage reduction applied to every incoming hit
     * @param energy     starting (and maximum) hit points
     */
    public Threat(String name, int skill, int resilience, int energy) {
        this.name = name;
        this.skill = skill;
        this.resilience = resilience;
        this.energy = energy;
        this.maxEnergy = energy;
    }

    /**
     * Returns the threat's fixed attack value. Unlike crew members, threats do not
     * have a random component — difficulty comes from scaling stats, not variance.
     *
     * @return the skill stat used as damage before the target's resilience is applied
     */
    public int act() {
        return skill;
    }

    /**
     * Reduces energy by the net damage after resilience absorption.
     * Energy is clamped to zero.
     *
     * @param incomingDamage raw attack roll from a crew member
     */
    public void defend(int incomingDamage) {
        int net = Math.max(0, incomingDamage - resilience);
        energy = Math.max(0, energy - net);
    }

    /** @return {@code true} while the threat has remaining energy */
    public boolean isAlive() {
        return energy > 0;
    }

    public String getName() { return name; }
    public int getSkill() { return skill; }
    public int getResilience() { return resilience; }
    public int getEnergy() { return energy; }
    public int getMaxEnergy() { return maxEnergy; }
}
