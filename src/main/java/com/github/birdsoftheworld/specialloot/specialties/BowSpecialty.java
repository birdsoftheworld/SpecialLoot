package com.github.birdsoftheworld.specialloot.specialties;

import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public interface BowSpecialty {
    boolean onFire(EntityShootBowEvent event);
    void onProjectileHit(ProjectileHitEvent event);
    void onFireworkExplode(FireworkExplodeEvent event);
}
