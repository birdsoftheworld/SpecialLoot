package com.github.birdsoftheworld.specialloot.specialties;

import com.github.birdsoftheworld.specialloot.util.SpecialtyProperties;
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

    public void registerSpecialties(Plugin plugin) {
        loadSpecialtiesConfig(plugin);
        Set<String> keys = specialtiesConfig.getKeys(false);

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

                Specialty registeredSpecialty = register(categoryKey, path + "." + key);

                processProperties(configSection, registeredSpecialty, plugin);
            }
        }
    }

    private void processProperties(ConfigurationSection configSection, Specialty specialty, Plugin plugin) {
        Set<String> propertyKeys = configSection.getKeys(false);

        for (String propertyKey : propertyKeys) {
            if (specialty.getDefaultProperties().isDefined(propertyKey)) {
                continue;
            }

            switch (propertyKey) {
                case "config":
                    String configPath = (String) configSection.get(propertyKey);

                    assert configPath != null;
                    File loadedConfigFile = new File(plugin.getDataFolder(), configPath);

                    YamlConfiguration config = loadYamlConfiguration(loadedConfigFile, configPath);
                    if (config == null) {
                        continue;
                    }
                    processProperties(config, specialty, plugin);
                    break;

                case "property-sets":
                    ConfigurationSection section = configSection.getConfigurationSection(propertyKey);

                    assert section != null;
                    processPropertySets(section, specialty);
                    break;

                default:
                    SpecialtyProperty newProperty = new SpecialtyProperty();
                    newProperty.setValue(configSection.get(propertyKey));

                    specialty.setProperty(propertyKey, newProperty);
                    break;
            }
        }
    }

    private void processPropertySets(ConfigurationSection section, Specialty specialty) {
        Set<String> propertySetKeys = section.getKeys(false);

        for (String propertySetName : propertySetKeys) {
            SpecialtyProperties propertySet = specialty.getDefaultProperties().clone();
            ConfigurationSection propertySetSection = section.getConfigurationSection(propertySetName);

            assert propertySetSection != null;
            Set<String> properties = propertySetSection.getKeys(false);
            for (String property : properties) {
                SpecialtyProperty newProperty = new SpecialtyProperty();
                newProperty.setValue(propertySetSection.get(property));

                propertySet.setProperty(property, newProperty);
            }

            specialty.setProperties(propertySetName, propertySet);
        }
    }

    private void loadSpecialtiesConfig(Plugin plugin) {
        File specialtiesConfigFile = new File(plugin.getDataFolder(), "specialties.yml");
        specialtiesConfig = loadYamlConfiguration(specialtiesConfigFile, "specialties.yml");
    }

    private YamlConfiguration loadYamlConfiguration(File file, String path) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(path, false);
        }

        YamlConfiguration yamlConfiguration = new YamlConfiguration();

        try {
            yamlConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            return null;
        }

        return yamlConfiguration;
    }

    public static List<Specialty> values() {
        return specialties;
    }

    public static Specialty valueOf(String specialty) {
        return specialtiesByName.get(specialty.toLowerCase());
    }
}
