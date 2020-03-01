package com.github.birdsoftheworld.specialloot.specialties.bows;

import com.github.birdsoftheworld.specialloot.specialties.BowSpecial;
import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import com.github.birdsoftheworld.specialloot.util.SpecialtyProperties;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.Plugin;

public class ExplodingBow extends Specialty implements BowSpecial {

    public ExplodingBow(Plugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onFire(EntityShootBowEvent event, SpecialtyProperties properties) {
        if (event.getProjectile() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getProjectile();
            arrow.setColor(Color.RED);
        }
        return true;
    }

    @Override
    public void onProjectileHit(ProjectileHitEvent event, SpecialtyProperties properties) {
        Projectile projectile = event.getEntity();
        Location hitLocation = projectile.getLocation();
        World world = projectile.getWorld();

        world.createExplosion(hitLocation, (float) (double) properties.getProperty("explosion-power").getValue());

        projectile.remove();
    }

    @Override
    public void onFireworkExplode(FireworkExplodeEvent event, SpecialtyProperties properties) {
        Firework firework = event.getEntity();
        Location hitLocation = firework.getLocation();
        World world = firework.getWorld();

        world.createExplosion(hitLocation, (float) (double) properties.getProperty("explosion-power").getValue());
    }
}
