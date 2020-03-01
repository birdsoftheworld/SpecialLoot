package com.github.birdsoftheworld.specialloot.listeners;

import com.github.birdsoftheworld.specialloot.util.SpecialItems;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class CraftingListener implements Listener {

    private SpecialItems specialItems;

    public CraftingListener(Plugin plugin) {
        this.specialItems = new SpecialItems(plugin);
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (specialItems.isSpecialItem(event.getCurrentItem())) {
            ItemStack result = event.getCurrentItem();
            specialItems.makeUnstackable(result);
            event.setCurrentItem(result);
            if (event.isShiftClick()) {
                event.setCancelled(true);
            }
        }
    }
}
