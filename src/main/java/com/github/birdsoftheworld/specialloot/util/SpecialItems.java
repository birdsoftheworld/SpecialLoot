package com.github.birdsoftheworld.specialloot.util;

import com.github.birdsoftheworld.specialloot.enchantments.Glint;
import com.github.birdsoftheworld.specialloot.specialties.Specialties;
import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpecialItems {
    private final NamespacedKey specialtiesKey;
    private final NamespacedKey randomKey;
    private final NamespacedKey maxUsesKey;
    private final NamespacedKey usesKey;
    public SpecialItems(Plugin plugin) {
        specialtiesKey = new NamespacedKey(plugin, "specialties");
        randomKey = new NamespacedKey(plugin, "random");
        maxUsesKey = new NamespacedKey(plugin, "maxUses");
        usesKey = new NamespacedKey(plugin, "uses");
    }

    public ItemStack createSpecialItem(ItemStack item, Plugin plugin) {
        ItemStack clonedItem = item.clone();
        ItemMeta meta = clonedItem.getItemMeta();

        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        PersistentDataAdapterContext context = container.getAdapterContext();

        PersistentDataContainer newContainer = context.newPersistentDataContainer();

        // set all specialties to disabled
        for(Specialty special : Specialties.values()) {
            NamespacedKey key = new NamespacedKey(plugin, special.getName());
            newContainer.set(key, PersistentDataType.BYTE, (byte) 0);
        }

        // set max uses
        newContainer.set(maxUsesKey, PersistentDataType.INTEGER, Integer.MAX_VALUE);

        // set uses
        newContainer.set(usesKey, PersistentDataType.INTEGER, Integer.MAX_VALUE);

        // add tag container to normal container
        container.set(specialtiesKey, PersistentDataType.TAG_CONTAINER, newContainer);

        // random value to prevent items from being stack-able
        container.set(randomKey, PersistentDataType.STRING, UUID.randomUUID().toString());

        clonedItem.setItemMeta(meta);

        return clonedItem;
    }

    public void setSpecialty(ItemStack item, Plugin plugin, Specialty specialty, boolean enabled) {
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        PersistentDataContainer holder = meta.getPersistentDataContainer();

        NamespacedKey specialtiesKey = new NamespacedKey(plugin, "specialties");
        PersistentDataContainer specialtyHolder = holder.get(specialtiesKey, PersistentDataType.TAG_CONTAINER);

        // error if item doesn't have the specialties container
        if(specialtyHolder == null) {
            throw new IllegalStateException("Item is not a SpecialItem!");
        }

        NamespacedKey key = new NamespacedKey(plugin, specialty.getName());

        // set if enabled
        specialtyHolder.set(key, PersistentDataType.BYTE, (byte) (enabled ? 1 : 0));

        // add other specialties' lores
        List<String> lores = new ArrayList<>();
        for (Specialty enabledSpecialty : getSpecialties(item, plugin)) {
            lores.add(enabledSpecialty.getProperties().getLore());
        }

        // add current specialty's lore
        lores.add(specialty.getProperties().getLore());

        // set max uses
        @SuppressWarnings("ConstantConditions")
        int currentMaxUses = specialtyHolder.get(maxUsesKey, PersistentDataType.INTEGER);
        int specialMaxUses = specialty.getProperties().getMaxUses();
        int finalMaxUses = Math.min(currentMaxUses, specialMaxUses);

        // set current uses
        @SuppressWarnings("ConstantConditions")
        int currentUses = specialtyHolder.get(usesKey, PersistentDataType.INTEGER);
        int finalUses = Math.min(currentUses, finalMaxUses);

        // set values for uses
        specialtyHolder.set(maxUsesKey, PersistentDataType.INTEGER, finalMaxUses);
        specialtyHolder.set(usesKey, PersistentDataType.INTEGER, finalUses);

        // set lores for uses
        lores.add(ChatColor.AQUA.toString() + "Uses: " + finalUses + " / " + finalMaxUses);

        holder.set(specialtiesKey, PersistentDataType.TAG_CONTAINER, specialtyHolder);

        meta.setLore(lores);

        item.setItemMeta(meta);
    }

    public List<Specialty> getSpecialties(ItemStack item, Plugin plugin) {
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        List<Specialty> specialtiesList = new ArrayList<>();

        NamespacedKey specialtiesKey = new NamespacedKey(plugin, "specialties");
        PersistentDataContainer specialtiesContainer = container.get(specialtiesKey, PersistentDataType.TAG_CONTAINER);

        // item is not special, thus has no specialties
        if(specialtiesContainer == null) {
            return specialtiesList;
        }

        // iterate through specialties and check if item has them
        for(Specialty special : Specialties.values()) {
            NamespacedKey key = new NamespacedKey(plugin, special.getName());
            byte specialEnabled = specialtiesContainer.getOrDefault(key, PersistentDataType.BYTE, (byte) 0);
            if(specialEnabled == (byte) 1) {
                specialtiesList.add(special);
            }
        }

        return specialtiesList;
    }

    public boolean isSpecialItem(ItemStack item, Plugin plugin) {
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(plugin, "specialties");

        return container.has(key, PersistentDataType.TAG_CONTAINER);
    }

    public void applySpecialProperties(ItemStack item, Plugin plugin) {
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        PersistentDataContainer specialtyContainer = container.get(specialtiesKey, PersistentDataType.TAG_CONTAINER);

        if (specialtyContainer == null) {
            throw new IllegalStateException("Item is not a SpecialItem!");
        }

        NamespacedKey enchantmentGlintKey = new NamespacedKey(plugin, "glint");
        Glint glint = new Glint (enchantmentGlintKey);

        for (Specialty specialty : Specialties.values()) {
            NamespacedKey key = new NamespacedKey(plugin, specialty.getName());

            // continue if disabled
            @SuppressWarnings("ConstantConditions")
            byte enabledByte = specialtyContainer.get(key, PersistentDataType.BYTE);
            if (enabledByte == 0) {
                continue;
            }

            // add glint
            if (specialty.getProperties().hasEnchantmentGlint()) {
                meta.addEnchant(glint, 0, true);
            }
        }

        item.setItemMeta(meta);
    }

    @SuppressWarnings("ConstantConditions")
    public boolean use(ItemStack item, Plugin plugin) {
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        PersistentDataContainer specialtyContainer = container.get(specialtiesKey, PersistentDataType.TAG_CONTAINER);

        int currentUses = specialtyContainer.get(usesKey, PersistentDataType.INTEGER);
        int maxUses = specialtyContainer.get(maxUsesKey, PersistentDataType.INTEGER);
        int finalUses = Math.min(currentUses, maxUses) - 1;

        specialtyContainer.set(usesKey, PersistentDataType.INTEGER, finalUses);

        // save uses
        container.set(specialtiesKey, PersistentDataType.TAG_CONTAINER, specialtyContainer);

        // add specialties' lores
        List<String> lores = new ArrayList<>();
        for (Specialty enabledSpecialty : getSpecialties(item, plugin)) {
            lores.add(enabledSpecialty.getProperties().getLore());
        }

        lores.add(ChatColor.AQUA.toString() + "Uses: " + finalUses + " / " + maxUses);

        meta.setLore(lores);

        item.setItemMeta(meta);

        return finalUses <= 0;
    }
}
