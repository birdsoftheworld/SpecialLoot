package com.github.birdsoftheworld.specialloot.listeners;

import com.github.birdsoftheworld.specialloot.listeners.specialties.BlockListener;
import com.github.birdsoftheworld.specialloot.listeners.specialties.BowListener;
import com.github.birdsoftheworld.specialloot.listeners.specialties.InteractListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ListenerManager {
    private Plugin plugin;

    public ListenerManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void registerListeners() {
        // for specialties
        Bukkit.getPluginManager().registerEvents(new InteractListener(plugin), plugin);
        Bukkit.getPluginManager().registerEvents(new BowListener(plugin), plugin);
        Bukkit.getPluginManager().registerEvents(new BlockListener(), plugin);

        // for crafting
        Bukkit.getPluginManager().registerEvents(new CraftingListener(plugin), plugin);

        // can't fix for now
//        Bukkit.getPluginManager().registerEvents(new LootableBlockCreateListener(), plugin);
//        Bukkit.getPluginManager().registerEvents(new LootListener(plugin), plugin);
    }
}
