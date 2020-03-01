package com.github.birdsoftheworld.specialloot.specialties;

import com.github.birdsoftheworld.specialloot.util.SpecialtyProperties;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public interface BowSpecial {
    boolean onFire(EntityShootBowEvent event, SpecialtyProperties properties);
    void onProjectileHit(ProjectileHitEvent event, SpecialtyProperties properties);
    void onFireworkExplode(FireworkExplodeEvent event, SpecialtyProperties properties);
}
