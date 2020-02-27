package com.github.birdsoftheworld.specialloot.specialties;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Specialties {

    private final String DEFAULT_CLASSPATH = "com.github.birdsoftheworld.specialitems.specialties";
    private Plugin plugin;
    private static List<Specialty> specialties = new ArrayList<>();
    private static HashMap<String, Specialty> specialtiesByName = new HashMap<>();
    private static HashMap<Specialty, NamespacedKey> keys = new HashMap<>();

    public Specialties(Plugin plugin) {
        this.plugin = plugin;
    }

    public void register(String specialty, String namespace) {
        String path = DEFAULT_CLASSPATH + "." + namespace + "." + specialty;

        Class<?> specialtyClass;
        try {
            specialtyClass = Class.forName(path);
        } catch (Exception e) {
            throw new IllegalStateException("Listed Specialty at " + path + " doesn't exist!");
        }

        Object loadedInstance;
        try {
            loadedInstance = specialtyClass.getDeclaredConstructor(Plugin.class).newInstance(plugin);
        } catch (Exception e) {
            throw new IllegalStateException("Listed Specialty class has no constructor taking Plugin!");
        }

        if (!(loadedInstance instanceof Specialty)) {
            throw new IllegalStateException("List Specialty doesn't extend Specialty!");
        }

        Specialty loadedSpecialty = (Specialty) loadedInstance;

        specialties.add(loadedSpecialty);
        specialtiesByName.put(specialty, loadedSpecialty);
        keys.put(loadedSpecialty, new NamespacedKey(plugin, specialty));
    }

    public void registerAll(List<String> specialties, String namespace) {
        for (String specialty : specialties) {
            register(specialty, namespace);
        }
    }

    public static List<Specialty> values() {
        return specialties;
    }

    public static Specialty valueOf(String specialty) {
        return specialtiesByName.getOrDefault(specialty, null);
    }

    private static NamespacedKey getKey(String specialtyName) {
        return getKey(specialtiesByName.getOrDefault(specialtyName, null));
    }

    public static NamespacedKey getKey(Specialty specialty) {
        return keys.getOrDefault(specialty, null);
    }
}
