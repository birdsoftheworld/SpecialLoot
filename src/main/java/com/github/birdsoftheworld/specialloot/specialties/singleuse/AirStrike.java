package com.github.birdsoftheworld.specialloot.specialties.singleuse;

import com.github.birdsoftheworld.specialloot.specialties.InteractSpecial;
import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public class AirStrike extends Specialty implements InteractSpecial {
    public AirStrike(Plugin plugin) {
        super(plugin, "Spawn TNT from above", true, 1);
    }

    @Override
    public boolean onInteract(PlayerInteractEvent event) {
        return true;
    }
}
