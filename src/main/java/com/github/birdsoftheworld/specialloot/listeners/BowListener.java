package com.github.birdsoftheworld.specialloot.listeners;

import com.github.birdsoftheworld.specialloot.specialties.BowSpecial;
import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import com.github.birdsoftheworld.specialloot.util.SpecialItems;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BowListener extends SpecialtyListener implements Listener {

    private Plugin plugin;
    private SpecialItems specialItems;
    private HashMap<Entity, List<BowSpecial>> activeProjectiles = new HashMap<>();

    public BowListener(Plugin plugin) {
        this.plugin = plugin;
        this.specialItems = new SpecialItems(plugin);
    }

    @EventHandler
    public void onFireArrow(EntityShootBowEvent event) {
        ItemStack bow = event.getBow();
        if (bow == null) {
            return;
        }

        if (!specialItems.isSpecialItem(bow)) {
            return;
        }

        LivingEntity entity = event.getEntity();

        if (!(entity instanceof Player)) {
            return;
        }

        Entity projectile = event.getProjectile();
        List<BowSpecial> specialties = new ArrayList<>();

        for (Specialty specialty : specialItems.getSpecialties(bow)) {
            if (specialty instanceof BowSpecial) {
                BowSpecial bowSpecial = (BowSpecial) specialty;
                specialties.add(bowSpecial);

                boolean useDurability = bowSpecial.onFire(event);
                if (useDurability) {
                    boolean itemBroke = use(bow, (Player) entity, specialItems, specialty);
                    if (itemBroke) {
                        // hack to remove crossbows 1 tick later because they somehow get re-added if removed
                        if (bow.getType().equals(Material.CROSSBOW)) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    ((Player) entity).getInventory().remove(bow);
                                }
                            }.runTaskLater(plugin, 1);
                        }
                    }
                }
            }
        }

        activeProjectiles.put(projectile, specialties);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();

        if (!activeProjectiles.containsKey(projectile)) {
            return;
        }

        List<BowSpecial> specialties = activeProjectiles.get(projectile);
        for (BowSpecial specialty : specialties) {
            specialty.onProjectileHit(event);
        }
    }

    @EventHandler
    public void onFireworkExplode(FireworkExplodeEvent event) {
        Firework firework = event.getEntity();

        if (!activeProjectiles.containsKey(firework)) {
            return;
        }

        List<BowSpecial> specialties = activeProjectiles.get(firework);
        for (BowSpecial specialty : specialties) {
            specialty.onFireworkExplode(event);
        }
    }
}
