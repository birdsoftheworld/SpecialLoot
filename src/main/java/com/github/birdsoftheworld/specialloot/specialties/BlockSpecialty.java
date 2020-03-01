package com.github.birdsoftheworld.specialloot.specialties;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public interface BlockSpecialty {
    void onBlockBreak(BlockBreakEvent event);
    void onBlockPlace(BlockPlaceEvent event);
}
