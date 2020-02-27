package com.github.birdsoftheworld.specialloot.specialties;

import com.github.birdsoftheworld.specialloot.util.SpecialtyProperties;
import org.bukkit.plugin.Plugin;

public abstract class Specialty {
    private String name;
    private SpecialtyProperties properties = new SpecialtyProperties();

    public Specialty(Plugin plugin, String lore, boolean hasEnchantmentGlint, int maxUses) {
        properties.setLore(lore);
        properties.setEnchantmentGlint(hasEnchantmentGlint);
        properties.setMaxUses(maxUses);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SpecialtyProperties getProperties() {
        return properties;
    }
}
