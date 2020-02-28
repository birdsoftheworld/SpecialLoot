package com.github.birdsoftheworld.specialloot.util;

import java.util.HashMap;

public class SpecialtyProperties {
    private HashMap<String, SpecialtyProperty> properties = new HashMap<>();

    public SpecialtyProperty getProperty(String key) {
        return properties.get(key);
    }

    public void setProperty(String key, SpecialtyProperty property) {
        properties.put(key, property);
    }
}
