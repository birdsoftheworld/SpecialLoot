package com.github.birdsoftheworld.specialloot.specialties;

import com.github.birdsoftheworld.specialloot.util.SpecialtyProperties;
import com.github.birdsoftheworld.specialloot.util.SpecialtyProperty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Specialty {

    private List<String[]> recipes = new ArrayList<>();
    private HashMap<String[], Material> recipeProducts = new HashMap<>();
    private HashMap<String[], String> recipePropertySets = new HashMap<>();
    private HashMap<String[], HashMap<String, Material>> recipeKeys = new HashMap<>();
    private HashMap<String[], String> recipeNames = new HashMap<>();

    private String name;
    private NamespacedKey key;
    private SpecialtyProperties defaultProperties = new SpecialtyProperties();
    protected Plugin plugin;
    private HashMap<String, SpecialtyProperties> propertySets = new HashMap<>();

    private boolean enabled;

    public Specialty(Plugin plugin) {
        this.plugin = plugin;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
        this.key = new NamespacedKey(plugin, name);
    }

    public boolean isEnabled() {
        return enabled;
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getLore() {
        return (String) getProperty("lore").getValue();
    }

    public int getMaxUses(String propertySet) {
        SpecialtyProperties properties = getProperties(propertySet);
        if (properties == null) {
            properties = getDefaultProperties();
        }
        return (int) properties.getProperty("max-uses").getValue();
    }

    public SpecialtyProperties getDefaultProperties() {
        return defaultProperties;
    }

    public void setDefaultProperties(SpecialtyProperties properties) {
        this.defaultProperties = properties;
    }

    public SpecialtyProperties getProperties(String propertySetName) {
        return propertySets.get(propertySetName);
    }

    public void setProperties(String propertySetName, SpecialtyProperties properties) {
        propertySets.put(propertySetName, properties);
    }

    public SpecialtyProperty getProperty(String property) {
        return defaultProperties.getProperty(property);
    }

    public SpecialtyProperty getPropertyOrDefault(String property, Object otherwise) {
        return defaultProperties.getPropertyOrDefault(property, otherwise);
    }

    public void setProperty(String property, SpecialtyProperty value) {
        defaultProperties.setProperty(property, value);
    }

    public NamespacedKey getKey() {
        return key;
    }

    void addCraftingRecipe(Material produces, String propertySet, String[] recipeString, HashMap<String, Material> key, String name) {
        recipes.add(recipeString);
        recipeProducts.put(recipeString, produces);
        recipePropertySets.put(recipeString, propertySet);
        recipeKeys.put(recipeString, key);
        recipeNames.put(recipeString, name);
    }

    public List<String[]> getRecipes() {
        return recipes;
    }

    public Material getRecipeProduct(String[] recipe) {
        return recipeProducts.get(recipe);
    }

    public String getRecipeProperties(String[] recipe) {
        return recipePropertySets.get(recipe);
    }

    public HashMap<String, Material> getRecipeKey(String[] recipe) {
        return recipeKeys.get(recipe);
    }

    public String getRecipeName(String[] recipe) {
        return recipeNames.get(recipe);
    }
}
