package com.github.birdsoftheworld.specialloot.events;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.loot.Lootable;

public class LootableBlockCreateListener implements Listener {
    @EventHandler
    public void onChunkPopulate(ChunkPopulateEvent event) {

        Chunk chunk = event.getChunk();
        BlockState[] tileEntities = chunk.getTileEntities();

        for (BlockState state : tileEntities) {
            if (state instanceof Container && state instanceof Lootable) {
                Lootable lootable = (Lootable) state;
                if (lootable.getLootTable() == null) {
                    return;
                }
                Bukkit.getPluginManager().callEvent(new LootableBlockCreateEvent(state));
            }
        }
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event) {

        Block block = event.getBlock();
        BlockState state = block.getState();

        if (state instanceof Container && state instanceof Lootable) {
            Lootable lootable = (Lootable) state;
            if (lootable.getLootTable() == null) {
                return;
            }
            Bukkit.getPluginManager().callEvent(new LootableBlockCreateEvent(state));
        }
    }
}
