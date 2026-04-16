package com.example.space_colony.model;

/**
 * Abstract base class for all crew member specializations.
 * <p>
 * Defines the shared state (skill, resilience, energy, experience, location) and
 * the combat contract ({@link #act()} / {@link #defend(int)}) used by the mission engine.
 * Concrete subclasses (Pilot, Engineer, Medic, Scientist, Soldier) supply their
 * base stats and specialization identity via {@link #getSpecializationName()} and
 * {@link #getImageResId()}.
 */
public abstract class CrewMember {
    protected int id;
    protected String name;
    protected int baseSkill;
    protected int resilience;
    protected int maxEnergy;
    protected int energy;
    protected int experience;
    protected Location location;

    /**
     * Initializes a new crew member at full energy in the Quarters location.
     *
     * @param name       display name chosen by the player
     * @param baseSkill  base attack power before experience bonuses
     * @param resilience damage reduction applied to every incoming hit
     * @param maxEnergy  maximum (and starting) hit points
     */
    protected CrewMember(String name, int baseSkill, int resilience, int maxEnergy) {
        this.name = name;
        this.baseSkill = baseSkill;
        this.resilience = resilience;
        this.maxEnergy = maxEnergy;
        this.energy = maxEnergy;
        this.experience = 0;
        this.location = Location.QUARTERS;
    }

    /**
     * Computes this member's attack roll for one mission turn.
     * Roll = baseSkill + experience + random bonus (0–2).
     *
     * @return attack value passed to the target's {@link #defend(int)}
     */
    public int act() {
        return baseSkill + experience + randomBonus();
    }

    /**
     * Applies incoming damage after subtracting this member's resilience.
     * Energy is clamped to zero; it never goes negative.
     *
     * @param incomingDamage raw attack roll received from the threat
     */
    public void defend(int incomingDamage) {
        int net = Math.max(0, incomingDamage - resilience);
        energy = Math.max(0, energy - net);
    }

    /**
     * Returns {@code true} while the member still has energy remaining.
     * A member with zero energy is considered defeated and is removed from Storage.
     */
    public boolean isAlive() {
        return energy > 0;
    }

    /** Fully restores energy to {@code maxEnergy}. Called when returning to Quarters. */
    public void restoreEnergy() {
        energy = maxEnergy;
    }

    /** Increments accumulated experience by 1. Awarded on successful mission completion. */
    public void gainExperience() {
        experience++;
    }

    /** Returns a random value in [0, 2] to add variance to each attack roll. */
    private int randomBonus() {
        return (int) (Math.random() * 3);
    }

    /** @return the drawable resource ID for this specialization's icon */
    public abstract int getImageResId();

    /** @return the human-readable specialization label (e.g. "Pilot") */
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
