package com.github.birdsoftheworld.specialloot.util;

import com.github.birdsoftheworld.specialloot.enums.Specialties;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class SpecialItems {
    public static ItemStack setSpecialty(ItemStack item, Plugin plugin, Specialties special, boolean enabled) {
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        PersistentDataContainer holder = meta.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(plugin, special.name());

        if(enabled) {
            holder.set(key, PersistentDataType.BYTE, (byte) 1);
        } else {
            holder.remove(key);
        }

        item.setItemMeta(meta);

        return item;
    }

    public static List<Specialties> getSpecialties(ItemStack item, Plugin plugin) {
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        List<Specialties> specialtiesList = new ArrayList<>();

        for(Specialties special : Specialties.values()) {
            NamespacedKey key = new NamespacedKey(plugin, special.name());
            byte specialEnabled = container.getOrDefault(key, PersistentDataType.BYTE, (byte) 0);
            if(specialEnabled == (byte) 1) {
                specialtiesList.add(special);
            }
        }

        return specialtiesList;
    }
}
