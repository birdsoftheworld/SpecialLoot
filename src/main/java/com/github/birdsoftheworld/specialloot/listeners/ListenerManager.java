package com.github.birdsoftheworld.specialloot.listeners;

import com.github.birdsoftheworld.specialloot.listeners.specialties.BlockListener;
import com.github.birdsoftheworld.specialloot.listeners.specialties.BowListener;
import com.github.birdsoftheworld.specialloot.listeners.specialties.InteractListener;
import com.github.birdsoftheworld.specialloot.listeners.specialties.ProjectileListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class ListenerManager {
    private Plugin plugin;

    public ListenerManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void registerListeners() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        // for specialties
        pluginManager.registerEvents(new InteractListener(plugin), plugin);
        pluginManager.registerEvents(new BowListener(plugin), plugin);
        pluginManager.registerEvents(new BlockListener(), plugin);
        pluginManager.registerEvents(new ProjectileListener(), plugin);

        // for crafting
        pluginManager.registerEvents(new CraftingListener(plugin), plugin);

        // can't fix for now
//        pluginManager.registerEvents(new LootableBlockCreateListener(), plugin);
//        pluginManager.registerEvents(new LootListener(plugin), plugin);
    }
}
