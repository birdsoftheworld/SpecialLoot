package com.github.birdsoftheworld.specialloot.util;

import org.bukkit.ChatColor;

public class SpecialtyProperties {
    private boolean hasEnchantmentGlint = false;
    private String lore;
    private int maxUses;

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

    public void setMaxUses(int maxUses) {
        this.maxUses = maxUses;
    }

    public int getMaxUses() {
        return maxUses;
    }
}
