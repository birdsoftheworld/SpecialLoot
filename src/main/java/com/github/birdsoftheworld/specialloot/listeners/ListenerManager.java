package com.github.birdsoftheworld.specialloot.listeners;

import com.github.birdsoftheworld.specialloot.events.LootableBlockCreateListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ListenerManager {
    private Plugin plugin;

    public ListenerManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new InteractListener(plugin), plugin);
        Bukkit.getPluginManager().registerEvents(new BowListener(plugin), plugin);
        Bukkit.getPluginManager().registerEvents(new ShieldListener(plugin), plugin);

        // can't fix for now
//        Bukkit.getPluginManager().registerEvents(new LootableBlockCreateListener(), plugin);
//        Bukkit.getPluginManager().registerEvents(new LootListener(plugin), plugin);
    }
}
