package com.github.birdsoftheworld.specialloot.specialties.consumable;

import com.github.birdsoftheworld.specialloot.specialties.InteractSpecial;
import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class KnockbackStick extends Specialty implements InteractSpecial {

    public KnockbackStick(Plugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onInteract(PlayerInteractEvent event) {
        return false;
    }

    @Override
    public boolean onInteractEntity(PlayerInteractEntityEvent event) {
        Entity interacted = event.getRightClicked();
        Player player = event.getPlayer();

        int knockbackMultiplier = (int) getPropertyOrDefault("knockback", 1).getValue();

        Vector knockback = player.getEyeLocation().getDirection().multiply(knockbackMultiplier).setY(1);
        interacted.setVelocity(knockback);
        return true;
    }
}
