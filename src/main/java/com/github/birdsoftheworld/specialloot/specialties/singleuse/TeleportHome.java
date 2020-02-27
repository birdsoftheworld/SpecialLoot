package com.github.birdsoftheworld.specialloot.specialties.singleuse;

import com.github.birdsoftheworld.specialloot.specialties.InteractSpecial;
import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class TeleportHome extends Specialty implements InteractSpecial {
    public TeleportHome(Plugin plugin) {
        super(plugin, "Teleport to your bed", true);
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            return;
        }

        EquipmentSlot hand = event.getHand();
        if (hand == null || hand.equals(EquipmentSlot.OFF_HAND)) {
            return;
        }

        Player player = event.getPlayer();
        Location teleportDestination = player.getBedSpawnLocation();

        // if player hasn't slept in a bed, teleport them to the world spawn
        if (teleportDestination == null) {
            teleportDestination = player.getWorld().getSpawnLocation();
        }

        player.teleport(teleportDestination);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1f, 1f);

        ItemStack item = event.getItem();
        if (item != null) {
            player.getInventory().remove(item);
        }
    }
}
