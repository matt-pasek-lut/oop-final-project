package com.example.space_colony.model;

public abstract class CrewMember {
    protected int id;
    protected String name;
    protected int baseSkill;
    protected int resilience;
    protected int maxEnergy;
    protected int energy;
    protected int experience;
    protected Location location;

    protected CrewMember(String name, int baseSkill, int resilience, int maxEnergy) {
        this.name = name;
        this.baseSkill = baseSkill;
        this.resilience = resilience;
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
        this.experience = 0;
        this.location = Location.QUARTERS;
    }

    public int act() {
        return baseSkill + experience + randomBonus();
    }

    public void defend(int incomingDamage) {
        int net = Math.max(0, incomingDamage - resilience);
        energy = Math.max(0, energy - net);
    }

    public boolean isAlive() {
        return energy > 0;
    }

    public void restoreEnergy() {
        energy = maxEnergy;
    }

    public void gainExperience() {
        experience++;
    }

    private int randomBonus() {
        return (int) (Math.random() * 3);
    }

    public abstract int getImageResId();

    public abstract String getSpecializationName();

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public int getSkill() { return baseSkill + experience; }
    public int getResilience() { return resilience; }
    public int getMaxEnergy() { return maxEnergy; }
    public int getEnergy() { return energy; }
    public void setEnergy(int energy) { this.energy = energy; }
    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
}
