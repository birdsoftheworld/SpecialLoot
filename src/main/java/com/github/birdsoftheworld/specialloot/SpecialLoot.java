package com.github.birdsoftheworld.specialloot;

import com.github.birdsoftheworld.specialloot.commands.AddSpecialty;
import com.github.birdsoftheworld.specialloot.listeners.SpecialItemListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SpecialLoot extends JavaPlugin {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        Bukkit.getPluginCommand("addspecialty").setExecutor(new AddSpecialty(this));
        Bukkit.getPluginManager().registerEvents(new SpecialItemListener(this), this);
    }
}
