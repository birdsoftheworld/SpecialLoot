package com.github.birdsoftheworld.specialloot.enums;

public enum Specialties {
    INSTANT_DEATH("Instantly kills anything hit"),
    AIR_STRIKE("Spawns TNT in a small radius"),
    TELEPORT_HOME("Teleports the player to their bed instantly");

    private String description;
    Specialties(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
