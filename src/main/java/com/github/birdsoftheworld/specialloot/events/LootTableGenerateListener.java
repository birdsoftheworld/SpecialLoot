package com.github.birdsoftheworld.specialloot.events;

import org.bukkit.Bukkit;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.loot.Lootable;

public class LootTableGenerateListener implements Listener {
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();
        if (holder instanceof Container && holder instanceof Lootable) {
            Lootable lootable = (Lootable) holder;
            if (lootable.getLootTable() == null) {
                // must be generating a loot table
                return;
            }
            Bukkit.getPluginManager().callEvent(new LootTableGenerateEvent((InventoryView) inventory, lootable));
        }
    }
}
