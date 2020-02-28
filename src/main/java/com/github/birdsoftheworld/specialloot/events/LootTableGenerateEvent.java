package com.github.birdsoftheworld.specialloot.events;

import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.InventoryView;

public class LootTableGenerateEvent extends InventoryEvent {
    public LootTableGenerateEvent(InventoryView transaction) {
        super(transaction);
    }
}
