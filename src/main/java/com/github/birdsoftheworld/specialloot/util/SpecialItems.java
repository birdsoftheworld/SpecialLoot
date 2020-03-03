package com.github.birdsoftheworld.specialloot.util;

import com.github.birdsoftheworld.specialloot.enchantments.Glint;
import com.github.birdsoftheworld.specialloot.specialties.Specialties;
import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
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
    private final NamespacedKey maxUsesKey;
    private final NamespacedKey usesKey;
    private final NamespacedKey randomKey;
    private final NamespacedKey enabledKey;
    private final NamespacedKey propertySetKey;
    private final Plugin plugin;

    public SpecialItems(Plugin plugin) {
        specialtiesKey = new NamespacedKey(plugin, "specialties");
        maxUsesKey = new NamespacedKey(plugin, "maxUses");
        usesKey = new NamespacedKey(plugin, "uses");
        randomKey = new NamespacedKey(plugin, "random");
        enabledKey = new NamespacedKey(plugin, "specialtyEnabled");
        propertySetKey = new NamespacedKey(plugin, "propertySet");
        this.plugin = plugin;
    }

    public ItemStack createSpecialItem(ItemStack item) {
        ItemStack clonedItem = item.clone();
        ItemMeta meta = clonedItem.getItemMeta();

        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();
        PersistentDataAdapterContext context = container.getAdapterContext();

        PersistentDataContainer newContainer = context.newPersistentDataContainer();
        PersistentDataAdapterContext newContainerContext = newContainer.getAdapterContext();

        // set all specialties to disabled
        for(Specialty special : Specialties.values()) {
            NamespacedKey key = special.getKey();
            PersistentDataContainer specialtyContainer = newContainerContext.newPersistentDataContainer();

            specialtyContainer.set(enabledKey, PersistentDataType.BYTE, (byte) 0);

            newContainer.set(key, PersistentDataType.TAG_CONTAINER, specialtyContainer);
        }

        // set max uses
        newContainer.set(maxUsesKey, PersistentDataType.INTEGER, Integer.MAX_VALUE);

        // set uses
        newContainer.set(usesKey, PersistentDataType.INTEGER, Integer.MAX_VALUE);

        // add tag container to normal container
        container.set(specialtiesKey, PersistentDataType.TAG_CONTAINER, newContainer);

        clonedItem.setItemMeta(meta);

        return clonedItem;
    }

    public void setSpecialty(ItemStack item, Specialty specialty, boolean enabled, String propertySet) {
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        // make item unbreakable
        meta.setUnbreakable((Boolean) specialty.getPropertyOrDefault("unbreakable", true).getValue());

        PersistentDataContainer holder = meta.getPersistentDataContainer();

        PersistentDataContainer specialtyHolder = holder.get(specialtiesKey, PersistentDataType.TAG_CONTAINER);

        // error if item doesn't have the specialties container
        if(specialtyHolder == null) {
            throw new IllegalStateException("Item is not a SpecialItem!");
        }

        NamespacedKey key = specialty.getKey();

        PersistentDataContainer propertyHolder = specialtyHolder.get(key, PersistentDataType.TAG_CONTAINER);

        // set if enabled
        assert propertyHolder != null;
        propertyHolder.set(enabledKey, PersistentDataType.BYTE, (byte) (enabled ? 1 : 0));

        // set propertySet
        if (propertySet == null) {
            propertySet = "default";
        }
        propertyHolder.set(propertySetKey, PersistentDataType.STRING, propertySet);

        specialtyHolder.set(key, PersistentDataType.TAG_CONTAINER, propertyHolder);

        // add other specialties' lores
        List<String> lores = new ArrayList<>();
        for (Specialty enabledSpecialty : getSpecialties(item)) {
            lores.add("Specialty: " + ChatColor.RESET.toString() + ChatColor.GOLD.toString() + enabledSpecialty.getLore());
        }

        // add current specialty's lore
        lores.add("Specialty: " + ChatColor.RESET.toString() + ChatColor.GOLD.toString() + specialty.getLore());

        SpecialtyProperties properties = specialty.getProperties(propertySet);
        if (properties == null) {
            properties = specialty.getDefaultProperties();
        }
        if (properties.isDefined("set-lore")) {
            lores.add("  - " + properties.getProperty("set-lore").getValue());
        }

        // set max uses
        @SuppressWarnings("ConstantConditions")
        int currentMaxUses = specialtyHolder.get(maxUsesKey, PersistentDataType.INTEGER);
        int specialMaxUses = specialty.getMaxUses(propertySet);
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

    public List<Specialty> getSpecialties(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        return getSpecialties(container);
    }

    public List<Specialty> getSpecialties(PersistentDataContainer container) {
        List<Specialty> specialtiesList = new ArrayList<>();

        PersistentDataContainer specialtiesContainer = container.get(specialtiesKey, PersistentDataType.TAG_CONTAINER);

        // item is not special, thus has no specialties
        if(specialtiesContainer == null) {
            return specialtiesList;
        }

        // iterate through specialties and check if item has them
        for(Specialty special : Specialties.values()) {
            NamespacedKey key = special.getKey();
            PersistentDataContainer propertyContainer = specialtiesContainer.get(key, PersistentDataType.TAG_CONTAINER);

            assert propertyContainer != null;
            byte specialEnabled = propertyContainer.getOrDefault(enabledKey, PersistentDataType.BYTE, (byte) 0);

            if(specialEnabled == (byte) 1) {
                specialtiesList.add(special);
            }
        }

        return specialtiesList;
    }

    public boolean isSpecialItem(ItemStack item) {
        if (item == null) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();

        if (meta == null) {
            return false;
        }

        PersistentDataContainer container = meta.getPersistentDataContainer();

        return isSpecial(container);
    }

    public boolean isSpecial(PersistentDataContainer container) {
        if (container == null) {
            return false;
        }
        return container.has(specialtiesKey, PersistentDataType.TAG_CONTAINER);
    }

    public void applySpecialProperties(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        PersistentDataContainer specialtyContainer = container.get(specialtiesKey, PersistentDataType.TAG_CONTAINER);

        if (specialtyContainer == null) {
            throw new IllegalArgumentException("Item is not a SpecialItem!");
        }

        NamespacedKey enchantmentGlintKey = new NamespacedKey(plugin, "glint");
        Glint glint = new Glint (enchantmentGlintKey);

        for (Specialty specialty : Specialties.values()) {
            NamespacedKey key = specialty.getKey();
            PersistentDataContainer propertyContainer = specialtyContainer.get(key, PersistentDataType.TAG_CONTAINER);

            // continue if disabled
            @SuppressWarnings("ConstantConditions")
            byte enabledByte = propertyContainer.get(enabledKey, PersistentDataType.BYTE);
            if (enabledByte == 0) {
                continue;
            }

            SpecialtyProperties properties = getPropertiesFor(item, specialty);

            if ((boolean) properties.getProperty("glint").getValue()) {
                // add glint
                meta.addEnchant(glint, 0, true);
            }
        }

        item.setItemMeta(meta);
    }

    public SpecialtyProperties getPropertiesFor(ItemStack item, Specialty specialty) {
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        PersistentDataContainer specialtyContainer = container.get(specialtiesKey, PersistentDataType.TAG_CONTAINER);
        if (specialtyContainer == null) {
            throw new IllegalArgumentException("Item is not a SpecialItem!");
        }

        NamespacedKey key = specialty.getKey();
        PersistentDataContainer propertyContainer = specialtyContainer.get(key, PersistentDataType.TAG_CONTAINER);

        assert propertyContainer != null;
        String propertySet = propertyContainer.get(propertySetKey, PersistentDataType.STRING);

        // get property set
        SpecialtyProperties properties = specialty.getProperties(propertySet);

        // if property set isn't defined, get defaults
        if (properties == null) {
            properties = specialty.getDefaultProperties();
        }

        return properties;
    }

    @SuppressWarnings("ConstantConditions")
    public boolean use(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        PersistentDataContainer specialtyContainer = container.get(specialtiesKey, PersistentDataType.TAG_CONTAINER);

        int currentUses = specialtyContainer.get(usesKey, PersistentDataType.INTEGER);
        int maxUses = specialtyContainer.get(maxUsesKey, PersistentDataType.INTEGER);
        int finalUses = currentUses - 1;

        specialtyContainer.set(usesKey, PersistentDataType.INTEGER, finalUses);

        // save uses
        container.set(specialtiesKey, PersistentDataType.TAG_CONTAINER, specialtyContainer);

        // add specialties' lores
        List<String> lores = new ArrayList<>();
        for (Specialty enabledSpecialty : getSpecialties(item)) {
            lores.add("Specialty: " + ChatColor.RESET.toString() + ChatColor.GOLD.toString() + enabledSpecialty.getLore());
            SpecialtyProperties properties = getPropertiesFor(item, enabledSpecialty);
            if (properties == null) {
                properties = enabledSpecialty.getDefaultProperties();
            }
            if (properties != null && properties.isDefined("set-lore")) {
                lores.add("  - " + properties.getProperty("set-lore").getValue());
            }
        }

        lores.add(ChatColor.AQUA.toString() + "Uses: " + finalUses + " / " + maxUses);

        meta.setLore(lores);

        item.setItemMeta(meta);

        return finalUses <= 0;
    }

    @SuppressWarnings("ConstantConditions")
    public void setUses(ItemStack item, int uses) {
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        PersistentDataContainer specialtyContainer = container.get(specialtiesKey, PersistentDataType.TAG_CONTAINER);
        int maxUses = specialtyContainer.get(maxUsesKey, PersistentDataType.INTEGER);

        specialtyContainer.set(usesKey, PersistentDataType.INTEGER, uses);

        // save uses
        container.set(specialtiesKey, PersistentDataType.TAG_CONTAINER, specialtyContainer);

        // add specialties' lores
        List<String> lores = new ArrayList<>();
        for (Specialty enabledSpecialty : getSpecialties(item)) {
            lores.add("Specialty: " + ChatColor.RESET.toString() + ChatColor.GOLD.toString() + enabledSpecialty.getLore());
            SpecialtyProperties properties = getPropertiesFor(item, enabledSpecialty);
            if (properties == null) {
                properties = enabledSpecialty.getDefaultProperties();
            }
            if (properties.isDefined("set-lore")) {
                lores.add("  - " + properties.getProperty("set-lore").getValue());
            }
        }

        lores.add(ChatColor.AQUA.toString() + "Uses: " + uses + " / " + maxUses);

        meta.setLore(lores);

        item.setItemMeta(meta);
    }

    public void makeUnstackable(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        PersistentDataContainer holder = meta.getPersistentDataContainer();

        holder.set(randomKey, PersistentDataType.STRING, UUID.randomUUID().toString());

        item.setItemMeta(meta);
    }
}
