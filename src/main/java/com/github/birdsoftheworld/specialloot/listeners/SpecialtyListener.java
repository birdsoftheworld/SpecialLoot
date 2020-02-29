package com.github.birdsoftheworld.specialloot.listeners;

import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import com.github.birdsoftheworld.specialloot.util.SpecialItems;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class SpecialtyListener {
    boolean use(ItemStack item, Player player, SpecialItems specialItems, Specialty specialty) {
        boolean itemDestroyed = specialItems.use(item);

        // stop if item broke
        if (itemDestroyed) {

            player.getInventory().remove(item);
            player.updateInventory();

            // play item breaking sound
            boolean shouldPlayBreakingSound = (boolean) specialty.getPropertyOrDefault("break-sound", false).getValue();
            if (shouldPlayBreakingSound) {
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f, 1f);
            }
            return true;
        }
        return false;
    }
}
