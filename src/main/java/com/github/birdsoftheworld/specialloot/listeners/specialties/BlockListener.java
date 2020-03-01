package com.github.birdsoftheworld.specialloot.listeners.specialties;

import com.github.birdsoftheworld.specialloot.specialties.BlockSpecial;
import com.github.birdsoftheworld.specialloot.specialties.Specialties;
import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener extends SpecialtyListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        for (Specialty specialty : Specialties.values()) {
            if (specialty instanceof BlockSpecial) {
                ((BlockSpecial) specialty).onBlockBreak(event);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        for (Specialty specialty : Specialties.values()) {
            if (specialty instanceof BlockSpecial) {
                ((BlockSpecial) specialty).onBlockPlace(event);
            }
        }
    }
}
