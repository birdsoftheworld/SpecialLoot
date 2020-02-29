package com.github.birdsoftheworld.specialloot.listeners;

import com.github.birdsoftheworld.specialloot.specialties.InteractSpecial;
import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import com.github.birdsoftheworld.specialloot.util.SpecialItems;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class InteractListener extends SpecialtyListener implements Listener {

    private final SpecialItems specialItems;

    public InteractListener(Plugin plugin) {
        this.specialItems = new SpecialItems(plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();

        if (item != null) {
            List<Specialty> specialties = specialItems.getSpecialties(item);
            for (Specialty specialty : specialties) {
                // run specialty actions
                if (specialty instanceof InteractSpecial) {
                    boolean useDurability = ((InteractSpecial) specialty).onInteract(event);

                    if (useDurability) {
                        Player player = event.getPlayer();
                        if (use(item, player, specialItems, specialty)) {
                            // will crash if event is not cancelled
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
