package com.github.birdsoftheworld.specialloot.events;

import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.loot.Lootable;

public class LootTableGenerateEvent extends InventoryEvent {

    private Lootable holder;

    public LootTableGenerateEvent(InventoryView transaction, Lootable holder) {
        super(transaction);
        this.holder = holder;
    }

    public Lootable getLootable() {
        return holder;
    }
}
