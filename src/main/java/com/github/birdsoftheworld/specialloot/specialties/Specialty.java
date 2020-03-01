package com.github.birdsoftheworld.specialloot.specialties;

import com.github.birdsoftheworld.specialloot.util.SpecialtyProperties;
import com.github.birdsoftheworld.specialloot.util.SpecialtyProperty;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public abstract class Specialty {
    private String name;
    private NamespacedKey key;
    private SpecialtyProperties defaultProperties = new SpecialtyProperties();
    protected Plugin plugin;
    private HashMap<String, SpecialtyProperties> propertySets = new HashMap<>();

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

    public int getMaxUses(String propertySet) {
        SpecialtyProperties properties = getProperties(propertySet);
        if (properties == null) {
            properties = getDefaultProperties();
        }
        return (int) properties.getProperty("max-uses").getValue();
    }

    public SpecialtyProperties getDefaultProperties() {
        return defaultProperties;
    }

    public void setDefaultProperties(SpecialtyProperties properties) {
        this.defaultProperties = properties;
    }

    public SpecialtyProperties getProperties(String propertySetName) {
        return propertySets.get(propertySetName);
    }

    public void setProperties(String propertySetName, SpecialtyProperties properties) {
        propertySets.put(propertySetName, properties);
    }

    public SpecialtyProperty getProperty(String property) {
        return defaultProperties.getProperty(property);
    }

    public SpecialtyProperty getPropertyOrDefault(String property, Object otherwise) {
        return defaultProperties.getPropertyOrDefault(property, otherwise);
    }

    public void setProperty(String property, SpecialtyProperty value) {
        defaultProperties.setProperty(property, value);
    }

    public NamespacedKey getKey() {
        return key;
    }
}
