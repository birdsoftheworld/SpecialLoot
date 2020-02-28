package com.github.birdsoftheworld.specialloot.specialties;

import com.github.birdsoftheworld.specialloot.util.SpecialtyProperties;
import com.github.birdsoftheworld.specialloot.util.SpecialtyProperty;
import org.bukkit.plugin.Plugin;

public abstract class Specialty {
    private String name;
    private SpecialtyProperties properties = new SpecialtyProperties();
    private Plugin plugin;

    public Specialty(Plugin plugin) {
        this.plugin = plugin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLore() {
        return (String) getProperty("lore").getValue();
    }

    public int getMaxUses() {
        return (int) getProperty("max-uses").getValue();
    }

    public SpecialtyProperties getProperties() {
        return properties;
    }

    public SpecialtyProperty getProperty(String property) {
        return properties.getProperty(property);
    }

    public void setProperty(String property, SpecialtyProperty value) {
        properties.setProperty(property, value);
    }
}
