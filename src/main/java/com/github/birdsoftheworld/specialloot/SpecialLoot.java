package com.github.birdsoftheworld.specialloot;

import com.github.birdsoftheworld.specialloot.commands.AddSpecialty;
import com.github.birdsoftheworld.specialloot.commands.ListSpecialties;
import com.github.birdsoftheworld.specialloot.enchantments.Glint;
import com.github.birdsoftheworld.specialloot.events.LootTableGenerateListener;
import com.github.birdsoftheworld.specialloot.listeners.LootListener;
import com.github.birdsoftheworld.specialloot.listeners.SpecialItemListener;
import com.github.birdsoftheworld.specialloot.specialties.Specialties;
import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import com.github.birdsoftheworld.specialloot.util.SpecialtyProperty;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Set;

public class SpecialLoot extends JavaPlugin {

    private final String DEFAULT_CLASSPATH = "com.github.birdsoftheworld.specialloot.specialties";

    private YamlConfiguration specialtiesConfig;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        registerCustomEnchantments();
        registerSpecialties();

        Bukkit.getPluginCommand("addspecialty").setExecutor(new AddSpecialty(this));
        Bukkit.getPluginCommand("listspecialties").setExecutor(new ListSpecialties());

        Bukkit.getPluginManager().registerEvents(new SpecialItemListener(this), this);
        Bukkit.getPluginManager().registerEvents(new LootTableGenerateListener(), this);
        Bukkit.getPluginManager().registerEvents(new LootListener(this), this);
    }

    private void registerSpecialties() {
        loadSpecialtiesConfig();
        Set<String> keys = specialtiesConfig.getKeys(false);
        Specialties registerSpecialties = new Specialties(this);

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

                processProperties(configSection, registeredSpecialty);
            }
        }
    }

    private void processProperties(ConfigurationSection configSection, Specialty specialty) {
        Set<String> propertyKeys = configSection.getKeys(false);

        for (String propertyKey : propertyKeys) {
            if (specialty.getProperties().isDefined(propertyKey)) {
                continue;
            }

            if (propertyKey.equals("config")) {
                String path = (String) configSection.get(propertyKey);
                assert path != null;
                File loadedConfigFile = new File(getDataFolder(), path);

                if (!loadedConfigFile.exists()) {
                    loadedConfigFile.getParentFile().mkdirs();
                    saveResource(path, false);
                }

                YamlConfiguration yamlConfiguration = new YamlConfiguration();

                try {
                    yamlConfiguration.load(loadedConfigFile);
                } catch (IOException | InvalidConfigurationException e) {
                    e.printStackTrace();
                    continue;
                }

                processProperties(yamlConfiguration, specialty);
            } else {
                SpecialtyProperty newProperty = new SpecialtyProperty();
                newProperty.setValue(configSection.get(propertyKey));

                specialty.setProperty(propertyKey, newProperty);
            }
        }
    }

    private void loadSpecialtiesConfig() {
        File specialtiesConfigFile = new File(getDataFolder(), "specialties.yml");
        if (!specialtiesConfigFile.exists()) {
            specialtiesConfigFile.getParentFile().mkdirs();
            saveResource("specialties.yml", false);
        }

        specialtiesConfig = new YamlConfiguration();
        try {
            specialtiesConfig.load(specialtiesConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void registerCustomEnchantments() {
        try {
            Field acceptingNew = Enchantment.class.getDeclaredField("acceptingNew");
            acceptingNew.setAccessible(true);
            acceptingNew.set(null, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Glint glint = new Glint(new NamespacedKey(this, "glint"));
            Enchantment.registerEnchantment(glint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
