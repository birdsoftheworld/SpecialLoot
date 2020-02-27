package com.github.birdsoftheworld.specialloot;

import com.github.birdsoftheworld.specialloot.commands.AddSpecialty;
import com.github.birdsoftheworld.specialloot.commands.ListSpecialties;
import com.github.birdsoftheworld.specialloot.enchantments.Glint;
import com.github.birdsoftheworld.specialloot.listeners.SpecialItemListener;
import com.github.birdsoftheworld.specialloot.specialties.Specialties;
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
import java.util.List;
import java.util.Set;

public class SpecialLoot extends JavaPlugin {

    private YamlConfiguration specialtiesConfig;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        registerCustomEnchantments();
        registerSpecialties();

        Bukkit.getPluginCommand("addspecialty").setExecutor(new AddSpecialty(this));
        Bukkit.getPluginCommand("listspecialties").setExecutor(new ListSpecialties());

        Bukkit.getPluginManager().registerEvents(new SpecialItemListener(this), this);
    }

    private void registerSpecialties() {
        loadSpecialtiesConfig();
        Set<String> keys = specialtiesConfig.getKeys(false);
        Specialties registerSpecialties = new Specialties(this);

        for (String key : keys) {
            ConfigurationSection category = specialtiesConfig.getConfigurationSection(key);

            String namespace = category.getString("namespace");
            if (namespace == null) {
                throw new IllegalStateException("Invalid key: no provided namespace!");
            }

            List<String> entries = category.getStringList("entries");

            registerSpecialties.registerAll(entries, namespace);
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
