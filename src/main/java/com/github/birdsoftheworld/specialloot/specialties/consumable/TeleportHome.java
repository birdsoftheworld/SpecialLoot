package com.github.birdsoftheworld.specialloot.specialties.consumable;

import com.github.birdsoftheworld.specialloot.specialties.InteractSpecial;
import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import com.github.birdsoftheworld.specialloot.util.SpecialtyProperties;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;

public class TeleportHome extends Specialty implements InteractSpecial {
    public TeleportHome(Plugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onInteract(PlayerInteractEvent event, SpecialtyProperties properties) {
        // only right clicks
        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            return false;
        }

        // only main hand
        EquipmentSlot hand = event.getHand();
        if (hand == null || hand.equals(EquipmentSlot.OFF_HAND)) {
            return false;
        }

        Player player = event.getPlayer();
        Location teleportDestination = player.getBedSpawnLocation();

        // if player hasn't slept in a bed, teleport them to the world spawn
        if (teleportDestination == null) {
            teleportDestination = player.getWorld().getSpawnLocation();
        }

        player.teleport(teleportDestination);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1f, 1f);

        return true;
    }

    @Override
    public boolean onInteractEntity(PlayerInteractEntityEvent event, SpecialtyProperties properties) {
        return false;
    }
}
