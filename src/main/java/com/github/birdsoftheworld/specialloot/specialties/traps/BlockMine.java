package com.github.birdsoftheworld.specialloot.specialties.traps;

import com.github.birdsoftheworld.specialloot.specialties.BlockSpecial;
import com.github.birdsoftheworld.specialloot.specialties.InteractSpecial;
import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import com.github.birdsoftheworld.specialloot.util.SpecialtyProperties;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class BlockMine extends Specialty implements InteractSpecial, BlockSpecial {

    private HashMap<Block, SpecialtyProperties> riggedBlocks = new HashMap<>();

    public BlockMine(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (riggedBlocks.containsKey(block)) {
            SpecialtyProperties properties = riggedBlocks.get(block);

            block.getWorld().createExplosion(block.getLocation(), (float) (double) properties.getProperty("explosion-power").getValue());

            riggedBlocks.remove(block);
        }
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {}

    @Override
    public boolean onInteract(PlayerInteractEvent event, SpecialtyProperties properties) {
        event.setCancelled(true);
        Block block = event.getClickedBlock();
        if (block == null) {
            return false;
        }

        if (riggedBlocks.containsKey(block)) {
            return false;
        }

        riggedBlocks.put(block, properties);

        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_TNT_PRIMED, 1f, 1f);

        return true;
    }

    @Override
    public boolean onInteractEntity(PlayerInteractEntityEvent event, SpecialtyProperties properties) {
        return false;
    }
}
