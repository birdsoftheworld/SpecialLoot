package com.github.birdsoftheworld.specialloot.util;

import java.util.HashMap;

public class SpecialtyProperties {
    private HashMap<String, SpecialtyProperty> properties = new HashMap<>();

    public SpecialtyProperty getProperty(String key) {
        return properties.get(key);
    }

    public SpecialtyProperty getPropertyOrDefault(String key, Object otherwise) {
        return properties.getOrDefault(key, new SpecialtyProperty(otherwise));
    }

    public void setProperty(String key, SpecialtyProperty property) {
        properties.put(key, property);
    }

    public boolean isDefined(String key) {
        return properties.containsKey(key);
    }

    @Override
    public SpecialtyProperties clone() {
        SpecialtyProperties clonedProperties = new SpecialtyProperties();
        for (String key : properties.keySet()) {
            clonedProperties.setProperty(key, properties.get(key));
        }
        return clonedProperties;
    }
}
