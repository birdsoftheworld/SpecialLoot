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

    private Plugin plugin;

    public SpecialItemListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null) {
            List<Specialties> specialties = SpecialItems.getSpecialties(item, plugin);
            for(Specialties special : specialties) {
                player.sendMessage(special.name());
            }
        }
    }
}
