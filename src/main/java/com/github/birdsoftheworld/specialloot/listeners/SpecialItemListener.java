package com.github.birdsoftheworld.specialloot.listeners;

import com.github.birdsoftheworld.specialloot.enums.Specialties;
import com.github.birdsoftheworld.specialloot.util.SpecialItems;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class SpecialItemListener implements Listener {

    private final Plugin plugin;
    private final SpecialItems specialItems;

    public SpecialItemListener(Plugin plugin) {
        this.plugin = plugin;
        this.specialItems = new SpecialItems(plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();

        if (item != null) {
            List<Specialties> specialties = specialItems.getSpecialties(item, plugin);
            for(Specialties special : specialties) {
                // run specialty actions
                special.onUsed(event);
            }
        }
    }
}
