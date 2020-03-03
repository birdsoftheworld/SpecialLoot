package com.github.birdsoftheworld.specialloot.specialties.consumable;

import com.github.birdsoftheworld.specialloot.specialties.InteractSpecial;
import com.github.birdsoftheworld.specialloot.specialties.ProjectileSpecial;
import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import com.github.birdsoftheworld.specialloot.util.SpecialtyProperties;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class BlockBomb extends Specialty implements ProjectileSpecial, InteractSpecial {

    private HashMap<Projectile, SpecialtyProperties> activeProjectiles = new HashMap<>();

    public BlockBomb(Plugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onInteract(PlayerInteractEvent event, SpecialtyProperties properties) {
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            return false;
        }

        event.setCancelled(true);
        Player player = event.getPlayer();
        Projectile projectile = player.launchProjectile(Snowball.class);

        activeProjectiles.put(projectile, properties);

        return true;
    }

    @Override
    public boolean onInteractEntity(PlayerInteractEntityEvent event, SpecialtyProperties properties) {
        return false;
    }

    @Override
    public void onProjectileLaunch(ProjectileLaunchEvent event) { }

    @Override
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!activeProjectiles.containsKey(event.getEntity())) {
            return;
        }

        Projectile projectile = event.getEntity();

        SpecialtyProperties properties = activeProjectiles.get(projectile);

        activeProjectiles.remove(event.getEntity());

        Location location = projectile.getLocation();

        // explosion effect, won't destroy blocks
        projectile.getWorld().createExplosion(location, 0f, false, false);

        int centerX = location.getBlockX();
        int centerY = location.getBlockY();
        int centerZ = location.getBlockZ();

        int radius = (int) properties.getProperty("radius").getValue();

        World world = projectile.getWorld();
        Material blockType = Material.getMaterial((String) properties.getProperty("block").getValue());

        // sphere of blocks
        for (int x = centerX - radius; x < centerX + radius; x++) {
            for (int y = centerY - radius; y < centerY + radius; y++) {
                for (int z = centerZ - radius; z < centerZ + radius; z++) {
                    double distance = (centerX - x) * (centerX - x) + (centerY - y) * (centerY - y) + (centerZ - z) * (centerZ - z);
                    if (distance < radius * radius) {
                        Block block = world.getBlockAt(x, y, z);
                        if (block.isPassable()) {
                            block.setType(blockType);
                        }
                    }
                }
            }
        }
    }
}
