package com.github.birdsoftheworld.specialloot.specialties;

import com.github.birdsoftheworld.specialloot.util.SpecialtyProperties;
import com.github.birdsoftheworld.specialloot.util.SpecialtyProperty;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public abstract class Specialty {
    private String name;
    private NamespacedKey key;
    private SpecialtyProperties properties = new SpecialtyProperties();
    protected Plugin plugin;

    public Specialty(Plugin plugin) {
        this.plugin = plugin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.key = new NamespacedKey(plugin, name);
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

    public SpecialtyProperty getPropertyOrDefault(String property, Object otherwise) {
        return properties.getPropertyOrDefault(property, otherwise);
    }

    public void setProperty(String property, SpecialtyProperty value) {
        properties.setProperty(property, value);
    }

    public NamespacedKey getKey() {
        return key;
    }
}
