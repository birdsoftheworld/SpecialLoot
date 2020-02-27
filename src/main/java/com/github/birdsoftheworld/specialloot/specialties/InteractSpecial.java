package com.github.birdsoftheworld.specialloot.specialties;

import org.bukkit.event.player.PlayerInteractEvent;

public interface InteractSpecial {
    boolean onInteract(PlayerInteractEvent event);
}
