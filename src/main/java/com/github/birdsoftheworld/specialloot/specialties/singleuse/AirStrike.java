package com.github.birdsoftheworld.specialloot.specialties.singleuse;

import com.github.birdsoftheworld.specialloot.specialties.InteractSpecial;
import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class AirStrike extends Specialty implements InteractSpecial {
    public AirStrike(Plugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onInteract(PlayerInteractEvent event) {
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

        final Location playerLocation = player.getLocation();
        final World playerWorld = player.getWorld();

        int explosions = (int) getProperty("explosions").getValue();
        int interval = (int) getProperty("interval").getValue();

        int blocksAbove = (int) getProperty("height").getValue();
        int radius = (int) getProperty("radius").getValue();

        Random random = new Random();

        for (int i = 0; i < explosions; i++) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    playerWorld.spawnEntity(playerLocation.clone().add(random.nextInt(radius * 2 + 1) - radius, blocksAbove, random.nextInt(radius * 2 + 1) - radius), EntityType.PRIMED_TNT);
                }
            }.runTaskLater(plugin, i * interval);
        }

        player.playSound(playerLocation, Sound.ENTITY_TNT_PRIMED, 1f, 1f);

        return true;
    }
}
