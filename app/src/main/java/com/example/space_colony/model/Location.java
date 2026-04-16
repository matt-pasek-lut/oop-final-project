package com.example.space_colony.model;

/**
 * The three locations a crew member can occupy.
 * <ul>
 *   <li>{@code QUARTERS} — home base; energy is restored when a member arrives here</li>
 *   <li>{@code SIMULATOR} — training area; members gain XP via the Train action</li>
 *   <li>{@code MISSION_CONTROL} — battle arena; members can be selected for missions</li>
 * </ul>
 */
public enum Location {
    QUARTERS, SIMULATOR, MISSION_CONTROL
}
