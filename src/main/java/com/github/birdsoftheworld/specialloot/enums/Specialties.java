package com.github.birdsoftheworld.specialloot.enums;

import org.bukkit.NamespacedKey;

public enum Specialties {
    INSTANT_DEATH("Instantly kills anything hit", true),
    AIR_STRIKE("Spawns TNT in a small radius", false),
    TELEPORT_HOME("Teleports the player to their bed instantly", true);

    private final String description;
    private final boolean enchantmentGlint;
    Specialties(String description, boolean enchantmentGlint) {
        this.description = description;
        this.enchantmentGlint = enchantmentGlint;
    }
    public String getDescription() {
        return description;
    }
    public boolean hasEnchantmentGlint() {
        return enchantmentGlint;
    }
}
