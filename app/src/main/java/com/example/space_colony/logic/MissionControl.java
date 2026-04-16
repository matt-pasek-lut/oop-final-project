package com.example.space_colony.logic;

import com.example.space_colony.model.CrewMember;
import com.example.space_colony.model.Location;
import com.example.space_colony.model.Threat;

import java.util.ArrayList;
import java.util.List;

/**
 * Stateless utility class that runs cooperative turn-based missions.
 * <p>
 * A mission pits two crew members against a system-generated {@link Threat}.
 * Each round both crew members attack in sequence; the threat retaliates after
 * each individual attack. A crew member whose energy reaches zero is permanently
 * removed from {@link Storage}. Surviving members gain +1 experience on victory.
 * <p>
 * Threat difficulty scales with {@code missionCount}: skill, resilience, and
 * energy all increase with each completed mission.
 */
public class MissionControl {
    private static int missionCount = 0;

    private static final String[] THREAT_NAMES = {
        "Asteroid Storm", "Fuel Leakage", "Solar Flares", "Alien Attack", "System Failure"
    };

    /**
     * Executes a full mission between two crew members and a generated threat.
     * <p><
     * Returns a list of log lines describing every round, suitable for
     * animated display in {@link com.example.space_colony.ui.MissionControlFragment}.
     * Lines prefixed with {@code ===} are section headers, {@code ---} are round
     * separators, and empty strings are visual spacers.
     *
     * @param memberA first crew member (attacks first each round)
     * @param memberB second crew member (attacks second each round)
     * @return ordered list of log lines to be displayed in the mission log
     */
    public static List<String> runMission(CrewMember memberA, CrewMember memberB) {
        List<String> log = new ArrayList<>();
        Threat threat = generateThreat();

        log.add("=== MISSION: " + threat.getName() + " ===");
        log.add("Threat: " + threat.getName()
            + " (skill:" + threat.getSkill()
            + " res:" + threat.getResilience()
            + " energy:" + threat.getEnergy() + "/" + threat.getMaxEnergy() + ")");
        log.add(formatCrew(memberA));
        log.add(formatCrew(memberB));
        log.add("");

        List<CrewMember> alive = new ArrayList<>();
        alive.add(memberA);
        alive.add(memberB);

        int round = 1;
        while (threat.isAlive() && !alive.isEmpty()) {
            log.add("--- Round " + round + " ---");
            List<CrewMember> fallen = new ArrayList<>();

            for (CrewMember member : alive) {
                if (!threat.isAlive()) break;

                int roll = member.act();
                int dealt = Math.max(0, roll - threat.getResilience());
                threat.defend(roll);
                log.add(member.getSpecializationName() + "(" + member.getName() + ") attacks"
                    + " [roll:" + roll + "] — dealt " + dealt
                    + " | Threat HP: " + threat.getEnergy() + "/" + threat.getMaxEnergy());

                if (threat.isAlive()) {
                    int retaliation = threat.act();
                    int memberDamage = Math.max(0, retaliation - member.getResilience());
                    member.defend(retaliation);
                    log.add("  Threat retaliates [" + retaliation + "] — dealt " + memberDamage
                        + " | " + member.getName() + " HP: " + member.getEnergy() + "/" + member.getMaxEnergy());

                    if (!member.isAlive()) {
                        log.add("  " + member.getName() + " has been defeated and removed from the crew.");
                        fallen.add(member);
                        Storage.getInstance().remove(member.getId());
                    }
                }
            }
            alive.removeAll(fallen);
            round++;
        }

        log.add("");
        if (!threat.isAlive()) {
            log.add("=== MISSION COMPLETE ===");
            log.add("The " + threat.getName() + " has been neutralized!");
            for (CrewMember m : alive) {
                m.gainExperience();
                m.setLocation(Location.MISSION_CONTROL);
                log.add(m.getName() + " gains 1 XP (total: " + m.getExperience() + ")");
            }
        } else {
            log.add("=== MISSION FAILED ===");
            log.add("All crew members lost.");
        }

        missionCount++;
        return log;
    }

    /**
     * Generates a threat whose stats scale with the number of previously completed missions.
     * Formula: skill = 4 + missionCount, resilience = 1 + missionCount/3, energy = 20 + missionCount*3.
     */
    private static Threat generateThreat() {
        String name = THREAT_NAMES[missionCount % THREAT_NAMES.length];
        int skill = 4 + missionCount;
        int resilience = 1 + missionCount / 3;
        int energy = 20 + missionCount * 3;
        return new Threat(name, skill, resilience, energy);
    }

    private static String formatCrew(CrewMember m) {
        return m.getSpecializationName() + "(" + m.getName() + ")"
            + " skill:" + m.getSkill()
            + " res:" + m.getResilience()
            + " xp:" + m.getExperience()
            + " HP:" + m.getEnergy() + "/" + m.getMaxEnergy();
    }

    public static int getMissionCount() { return missionCount; }
    public static void setMissionCount(int count) { missionCount = count; }
}
