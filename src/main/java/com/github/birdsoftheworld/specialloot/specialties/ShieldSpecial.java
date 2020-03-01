package com.github.birdsoftheworld.specialloot.specialties;

import org.bukkit.event.entity.EntityDamageByEntityEvent;

public interface ShieldSpecial {
    boolean onShieldBlock(EntityDamageByEntityEvent event);
}
