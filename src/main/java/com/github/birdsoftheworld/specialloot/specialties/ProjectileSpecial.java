package com.github.birdsoftheworld.specialloot.specialties;

import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public interface ProjectileSpecial {
    void onProjectileLaunch(ProjectileLaunchEvent event);
    void onProjectileHit(ProjectileHitEvent event);
}
