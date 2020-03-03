package com.github.birdsoftheworld.specialloot.specialties;

import com.github.birdsoftheworld.specialloot.util.SpecialtyProperties;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public interface BowSpecial {
    boolean onBowFire(EntityShootBowEvent event, SpecialtyProperties properties);
    void onBowProjectileHit(ProjectileHitEvent event, SpecialtyProperties properties);
    void onFireworkExplode(FireworkExplodeEvent event, SpecialtyProperties properties);
}
