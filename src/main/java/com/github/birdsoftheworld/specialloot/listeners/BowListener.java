package com.github.birdsoftheworld.specialloot.listeners;

import com.github.birdsoftheworld.specialloot.specialties.BowSpecialty;
import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import com.github.birdsoftheworld.specialloot.util.SpecialItems;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BowListener extends SpecialtyListener implements Listener {

    private Plugin plugin;
    private SpecialItems specialItems;
    private HashMap<Entity, List<BowSpecialty>> activeProjectiles = new HashMap<>();

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
        List<BowSpecialty> specialties = new ArrayList<>();

        for (Specialty specialty : specialItems.getSpecialties(bow)) {
            if (specialty instanceof BowSpecialty) {
                BowSpecialty bowSpecialty = (BowSpecialty) specialty;
                specialties.add(bowSpecialty);

                boolean useDurability = bowSpecialty.onFire(event);
                if (useDurability) {
                    // FIXME: Crossbows don't break
                    use(bow, (Player) entity, specialItems, specialty);
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

        List<BowSpecialty> specialties = activeProjectiles.get(projectile);
        for (BowSpecialty specialty : specialties) {
            specialty.onProjectileHit(event);
        }
    }

    @EventHandler
    public void onFireworkExplode(FireworkExplodeEvent event) {
        Firework firework = event.getEntity();

        if (!activeProjectiles.containsKey(firework)) {
            return;
        }

        List<BowSpecialty> specialties = activeProjectiles.get(firework);
        for (BowSpecialty specialty : specialties) {
            specialty.onFireworkExplode(event);
        }
    }
}
