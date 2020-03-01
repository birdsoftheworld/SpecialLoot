package com.github.birdsoftheworld.specialloot.specialties;

import com.github.birdsoftheworld.specialloot.util.SpecialtyProperty;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Specialties {

    private Plugin plugin;
    private static List<Specialty> specialties = new ArrayList<>();
    private static HashMap<String, Specialty> specialtiesByName = new HashMap<>();
    private static HashMap<Specialty, NamespacedKey> keys = new HashMap<>();

    private final static String DEFAULT_CLASSPATH = "com.github.birdsoftheworld.specialloot.specialties";

    private static YamlConfiguration specialtiesConfig;

    public Specialties(Plugin plugin) {
        this.plugin = plugin;
    }

    public Specialty register(String specialty, String path) {

        path = path + "." + specialty;

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

        loadedSpecialty.setName(specialty);

        specialties.add(loadedSpecialty);
        specialtiesByName.put(specialty.toLowerCase(), loadedSpecialty);
        keys.put(loadedSpecialty, new NamespacedKey(plugin, specialty.toLowerCase()));

        return loadedSpecialty;
    }

    public static void registerSpecialties(Plugin plugin) {
        loadSpecialtiesConfig(plugin);
        Set<String> keys = specialtiesConfig.getKeys(false);
        Specialties registerSpecialties = new Specialties(plugin);

        for (String key : keys) {
            // refers to top level category, i.e. "singleuse"
            ConfigurationSection section = specialtiesConfig.getConfigurationSection(key);

            assert section != null;
            Set<String> categoryKeys = section.getKeys(false);

            for (String categoryKey : categoryKeys) {
                // refers to specialty configurations themselves, i.e. "AirStrike"
                ConfigurationSection configSection = section.getConfigurationSection(categoryKey);

                assert configSection != null;
                String path = configSection.getString("path");
                if (path == null) {
                    path = DEFAULT_CLASSPATH;
                }

                Specialty registeredSpecialty = registerSpecialties.register(categoryKey, path + "." + key);

                processProperties(configSection, registeredSpecialty, plugin);
            }
        }
    }

    private static void processProperties(ConfigurationSection configSection, Specialty specialty, Plugin plugin) {
        Set<String> propertyKeys = configSection.getKeys(false);

        for (String propertyKey : propertyKeys) {
            if (specialty.getProperties().isDefined(propertyKey)) {
                continue;
            }

            if (propertyKey.equals("config")) {
                String path = (String) configSection.get(propertyKey);
                assert path != null;
                File loadedConfigFile = new File(plugin.getDataFolder(), path);

                if (!loadedConfigFile.exists()) {
                    loadedConfigFile.getParentFile().mkdirs();
                    plugin.saveResource(path, false);
                }

                YamlConfiguration yamlConfiguration = new YamlConfiguration();

                try {
                    yamlConfiguration.load(loadedConfigFile);
                } catch (IOException | InvalidConfigurationException e) {
                    e.printStackTrace();
                    continue;
                }

                processProperties(yamlConfiguration, specialty, plugin);
            } else {
                SpecialtyProperty newProperty = new SpecialtyProperty();
                newProperty.setValue(configSection.get(propertyKey));

                specialty.setProperty(propertyKey, newProperty);
            }
        }
    }

    private static void loadSpecialtiesConfig(Plugin plugin) {
        File specialtiesConfigFile = new File(plugin.getDataFolder(), "specialties.yml");
        if (!specialtiesConfigFile.exists()) {
            specialtiesConfigFile.getParentFile().mkdirs();
            plugin.saveResource("specialties.yml", false);
        }

        specialtiesConfig = new YamlConfiguration();
        try {
            specialtiesConfig.load(specialtiesConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static List<Specialty> values() {
        return specialties;
    }

    public static Specialty valueOf(String specialty) {
        return specialtiesByName.get(specialty.toLowerCase());
    }

    private static NamespacedKey getKey(String specialtyName) {
        return getKey(specialtiesByName.getOrDefault(specialtyName, null));
    }

    public static NamespacedKey getKey(Specialty specialty) {
        return keys.getOrDefault(specialty, null);
    }
}
