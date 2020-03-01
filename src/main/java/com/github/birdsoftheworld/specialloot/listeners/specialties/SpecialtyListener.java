package com.github.birdsoftheworld.specialloot.listeners.specialties;

import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import com.github.birdsoftheworld.specialloot.util.SpecialItems;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

class SpecialtyListener {
    boolean use(ItemStack item, Player player, SpecialItems specialItems, Specialty specialty) {
        boolean itemDestroyed = specialItems.use(item);

        // stop if item broke
        if (itemDestroyed) {

            PlayerInventory inventory = player.getInventory();
            inventory.remove(item);

            // offhand isn't a storage slot, so it must be removed individually
            if (player.getInventory().getItemInOffHand().equals(item)) {
                player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
            }
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
