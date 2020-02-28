package com.github.birdsoftheworld.specialloot.events;

import org.bukkit.block.BlockState;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;

public class LootableBlockCreateEvent extends BlockEvent {

    private static final HandlerList handlers = new HandlerList();
    private BlockState blockState;

    public LootableBlockCreateEvent(BlockState blockState) {
        super(blockState.getBlock());
        this.blockState = blockState;
    }

    public BlockState getBlockState() {
        return blockState;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
