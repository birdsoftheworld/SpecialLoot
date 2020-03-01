package com.github.birdsoftheworld.specialloot;

import com.github.birdsoftheworld.specialloot.commands.AddSpecialty;
import com.github.birdsoftheworld.specialloot.commands.ListSpecialties;
import com.github.birdsoftheworld.specialloot.enchantments.Glint;
import com.github.birdsoftheworld.specialloot.listeners.ListenerManager;
import com.github.birdsoftheworld.specialloot.specialties.Specialties;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class SpecialLoot extends JavaPlugin {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        registerCustomEnchantments();
        Specialties.registerSpecialties(this);

        Bukkit.getPluginCommand("addspecialty").setExecutor(new AddSpecialty(this));
        Bukkit.getPluginCommand("listspecialties").setExecutor(new ListSpecialties());

        ListenerManager listenerManager = new ListenerManager(this);
        listenerManager.registerListeners();
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
