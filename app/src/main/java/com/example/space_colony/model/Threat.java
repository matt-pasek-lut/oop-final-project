package com.example.space_colony.model;

public class Threat {
    private final String name;
    private final int skill;
    private final int resilience;
    private int energy;
    private final int maxEnergy;

    public Threat(String name, int skill, int resilience, int energy) {
        this.name = name;
        this.skill = skill;
        this.resilience = resilience;
        this.energy = energy;
        this.maxEnergy = energy;
    }

    public int act() {
        return skill;
    }

    public void defend(int incomingDamage) {
        int net = Math.max(0, incomingDamage - resilience);
        energy = Math.max(0, energy - net);
    }

    public boolean isAlive() {
        return energy > 0;
    }

    public String getName() { return name; }
    public int getSkill() { return skill; }
    public int getResilience() { return resilience; }
    public int getEnergy() { return energy; }
    public int getMaxEnergy() { return maxEnergy; }
}
