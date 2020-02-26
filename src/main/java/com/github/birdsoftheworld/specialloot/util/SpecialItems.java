package com.github.birdsoftheworld.specialloot.util;

import com.github.birdsoftheworld.specialloot.enums.Specialties;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class SpecialItems {

    public static ItemStack createSpecialItem(ItemStack item, Plugin plugin) {
        ItemStack clonedItem = item.clone();
        ItemMeta meta = clonedItem.getItemMeta();

        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        PersistentDataAdapterContext context = container.getAdapterContext();

        NamespacedKey specialtiesKey = new NamespacedKey(plugin, "specialties");

        PersistentDataContainer newContainer = context.newPersistentDataContainer();

        // set all specialties to disabled
        for(Specialties special : Specialties.values()) {
            NamespacedKey key = new NamespacedKey(plugin, special.name());
            newContainer.set(key, PersistentDataType.BYTE, (byte) 0);
        }

        container.set(specialtiesKey, PersistentDataType.TAG_CONTAINER, newContainer);

        clonedItem.setItemMeta(meta);

        return clonedItem;
    }

    public static ItemStack setSpecialty(ItemStack item, Plugin plugin, Specialties special, boolean enabled) {
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        PersistentDataContainer holder = meta.getPersistentDataContainer();

        NamespacedKey specialtiesKey = new NamespacedKey(plugin, "specialties");
        PersistentDataContainer specialtyHolder = holder.get(specialtiesKey, PersistentDataType.TAG_CONTAINER);

        if(specialtyHolder == null) {
            throw new IllegalStateException("Item is not a SpecialItem!");
        }

        NamespacedKey key = new NamespacedKey(plugin, special.name());

        specialtyHolder.set(key, PersistentDataType.BYTE, (byte) (enabled ? 1 : 0));

        holder.set(specialtiesKey, PersistentDataType.TAG_CONTAINER, specialtyHolder);

        item.setItemMeta(meta);

        return item;
    }

    public static List<Specialties> getSpecialties(ItemStack item, Plugin plugin) {
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        List<Specialties> specialtiesList = new ArrayList<>();

        NamespacedKey specialtiesKey = new NamespacedKey(plugin, "specialties");
        PersistentDataContainer specialtiesContainer = container.get(specialtiesKey, PersistentDataType.TAG_CONTAINER);

        // item is not special, thus has no specialties
        if(specialtiesContainer == null) {
            return specialtiesList;
        }

        // iterate through specialties and check if item has them
        for(Specialties special : Specialties.values()) {
            NamespacedKey key = new NamespacedKey(plugin, special.name());
            byte specialEnabled = specialtiesContainer.getOrDefault(key, PersistentDataType.BYTE, (byte) 0);
            if(specialEnabled == (byte) 1) {
                specialtiesList.add(special);
            }
        }

        return specialtiesList;
    }

    public static boolean isSpecialItem(ItemStack item, Plugin plugin) {
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(plugin, "specialties");

        return container.has(key, PersistentDataType.TAG_CONTAINER);
    }
}
