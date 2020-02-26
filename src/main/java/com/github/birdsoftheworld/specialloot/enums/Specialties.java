package com.github.birdsoftheworld.specialloot.enums;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public enum Specialties {

    AIR_STRIKE("Spawns TNT in a small radius", false, "Specialty: Rain TNT from the sky") {
        @Override
        public void onUsed(PlayerInteractEvent event) {

        }
    },

    TELEPORT_HOME("Teleports the player to their bed instantly", true, "Specialty: Instantly teleport home") {
        @Override
        public void onUsed(PlayerInteractEvent event) {

            // only right clicks
            if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                return;
            }

            // don't do item actions
            event.setCancelled(true);

            Player player = event.getPlayer();
            Location teleportDestination = player.getBedSpawnLocation();

            // player's bedSpawnLocation is null if they haven't slept
            if (teleportDestination == null) {
                teleportDestination = player.getWorld().getSpawnLocation();
            }

            // teleport the player to their spawn, or the world spawn if their spawn is unset
            player.teleport(teleportDestination);

            ItemStack item = event.getItem();

            if (item != null) {
                // delete the item
                player.getInventory().remove(item);
            }

            // play a "vwoop" sound
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1f, 1f);
        }
    };

    private final String description;
    private final boolean enchantmentGlint;
    private final String lore;

    Specialties(String description, boolean enchantmentGlint, String lore) {
        this.description = description;
        this.enchantmentGlint = enchantmentGlint;
        this.lore = ChatColor.GOLD.toString() + lore;
    }

    public String getDescription() {
        return description;
    }

    public String getLore() {
        return lore;
    }

    public boolean hasEnchantmentGlint() {
        return enchantmentGlint;
    }

    public abstract void onUsed(PlayerInteractEvent event);
}
