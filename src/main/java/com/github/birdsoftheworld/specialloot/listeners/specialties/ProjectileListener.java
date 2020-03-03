package com.github.birdsoftheworld.specialloot.listeners.specialties;

import com.github.birdsoftheworld.specialloot.specialties.ProjectileSpecial;
import com.github.birdsoftheworld.specialloot.specialties.Specialties;
import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class ProjectileListener extends SpecialtyListener implements Listener {
    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        for (Specialty specialty : Specialties.values()) {
            if (specialty instanceof ProjectileSpecial) {
                ((ProjectileSpecial) specialty).onProjectileLaunch(event);
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        for (Specialty specialty : Specialties.values()) {
            if (specialty instanceof ProjectileSpecial) {
                ((ProjectileSpecial) specialty).onProjectileHit(event);
            }
        }
    }
}
