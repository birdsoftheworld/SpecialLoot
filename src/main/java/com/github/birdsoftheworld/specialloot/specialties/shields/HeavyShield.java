package com.github.birdsoftheworld.specialloot.specialties.shields;

import com.github.birdsoftheworld.specialloot.specialties.ShieldSpecial;
import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class HeavyShield extends Specialty implements ShieldSpecial {
    public HeavyShield(Plugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onShieldBlock(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        Entity victim = event.getEntity();
        int knockbackMultiplier = (int) getPropertyOrDefault("knockback", 1).getValue();
        Vector kb = victim.getLocation().toVector().subtract(attacker.getLocation().toVector()).multiply(-knockbackMultiplier);
        attacker.setVelocity(kb);
        return true;
    }
}
