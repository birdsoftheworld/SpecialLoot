package com.github.birdsoftheworld.specialloot;

import com.github.birdsoftheworld.specialloot.commands.AddPropertySet;
import com.github.birdsoftheworld.specialloot.commands.AddSpecialty;
import com.github.birdsoftheworld.specialloot.commands.ListSpecialties;
import com.github.birdsoftheworld.specialloot.crafting.CraftingManager;
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

        Specialties specialties = new Specialties(this);
        specialties.registerSpecialties(this);

        Bukkit.getPluginCommand("addspecialty").setExecutor(new AddSpecialty(this));
        Bukkit.getPluginCommand("listspecialties").setExecutor(new ListSpecialties());
        Bukkit.getPluginCommand("addpropertyset").setExecutor(new AddPropertySet());

        ListenerManager listenerManager = new ListenerManager(this);
        listenerManager.registerListeners();

        CraftingManager craftingManager = new CraftingManager(this);
        craftingManager.registerRecipes();
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
            NamespacedKey key = new NamespacedKey(this, "glint");

            // if enchantment is already defined, we can skip re-defining it
            if (Enchantment.getByKey(key) != null) {
                return;
            }

            Glint glint = new Glint(key);
            Enchantment.registerEnchantment(glint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
