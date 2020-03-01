package com.github.birdsoftheworld.specialloot.specialties;

import com.github.birdsoftheworld.specialloot.util.SpecialtyProperties;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public interface InteractSpecial {
    boolean onInteract(PlayerInteractEvent event, SpecialtyProperties properties);
    boolean onInteractEntity(PlayerInteractEntityEvent event, SpecialtyProperties properties);
}
