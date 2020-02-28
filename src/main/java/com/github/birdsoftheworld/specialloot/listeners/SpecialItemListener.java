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
import org.bukkit.inventory.PlayerInventory;
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
            List<Specialty> specialties = specialItems.getSpecialties(item, plugin);
            for (Specialty specialty : specialties) {
                // run specialty actions
                if (specialty instanceof InteractSpecial) {
                    boolean useDurability = ((InteractSpecial) specialty).onInteract(event);

                    if (useDurability) {
                        Player player = event.getPlayer();
                        PlayerInventory playerInventory = player.getInventory();

                        boolean itemDestroyed = specialItems.use(item, plugin);
                        player.updateInventory();

                        // stop if item broke
                        if (itemDestroyed) {
                            // will crash if event is not cancelled
                            event.setCancelled(true);
                            player.getInventory().remove(item);

                            // play item breaking sound
                            boolean shouldPlayBreakingSound = (boolean) specialty.getPropertyOrDefault("break-sound", false).getValue();
                            if (shouldPlayBreakingSound) {
                                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 1f);
                            }
                            return;
                        }
                    }
                }
            }
        }
    }
}
