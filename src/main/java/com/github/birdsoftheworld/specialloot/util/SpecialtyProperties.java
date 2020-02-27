package com.github.birdsoftheworld.specialloot.util;

import org.bukkit.ChatColor;

public class SpecialtyProperties {
    private boolean hasEnchantmentGlint = false;
    private String lore;

    public void setEnchantmentGlint(boolean hasEnchantmentGlint) {
        this.hasEnchantmentGlint = hasEnchantmentGlint;
    }

    public boolean hasEnchantmentGlint() {
        return hasEnchantmentGlint;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

    public String getLore() {
        return "Specialty: " + ChatColor.GOLD.toString() + lore;
    }

    public String getLoreRaw() {
        return lore;
    }
}
