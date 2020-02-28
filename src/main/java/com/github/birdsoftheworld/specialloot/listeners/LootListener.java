package com.github.birdsoftheworld.specialloot.listeners;

import com.github.birdsoftheworld.specialloot.events.LootableBlockCreateEvent;
import com.github.birdsoftheworld.specialloot.specialties.Specialties;
import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import com.github.birdsoftheworld.specialloot.util.SpecialItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;
import org.bukkit.plugin.Plugin;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LootListener implements Listener {
    private Random random = new Random();
    private SpecialItems specialItems;

    public LootListener(Plugin plugin) {
        specialItems = new SpecialItems(plugin);
    }

    @EventHandler
    public void onLootGenerate(LootableBlockCreateEvent event) {
        BlockState state = event.getBlockState();

        Container container = (Container) state;
        Lootable lootable = (Lootable) state;

        Inventory inventory = container.getInventory();
        LootTable lootTable = lootable.getLootTable();

        LootContext.Builder contextBuilder = new LootContext.Builder(event.getBlock().getLocation());
        LootContext context = contextBuilder.build();

        assert lootTable != null;
        lootTable.fillInventory(inventory, random, context);

        int slot = random.nextInt(inventory.getSize() + 1);

        ItemStack generatedItem = specialItems.createSpecialItem(new ItemStack(Material.ENDER_EYE));
        List<Specialty> specialties = Specialties.values();
        Collections.shuffle(specialties);
        specialItems.setSpecialty(generatedItem, specialties.get(0), true);
        inventory.setItem(slot, generatedItem);

        // prevent vanilla populating
        lootable.setLootTable(null);
    }
}
