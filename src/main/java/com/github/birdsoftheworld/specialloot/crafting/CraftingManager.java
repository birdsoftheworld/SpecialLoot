package com.github.birdsoftheworld.specialloot.crafting;

import com.github.birdsoftheworld.specialloot.specialties.Specialties;
import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import com.github.birdsoftheworld.specialloot.util.SpecialItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class CraftingManager {

    private static List<Recipe> registeredRecipes = new ArrayList<>();
    private Plugin plugin;

    public CraftingManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void registerRecipes() {
        List<Specialty> specialties = Specialties.values();

        for (Specialty specialty : specialties) {

            // ignore disabled specialties
            if (specialty.isEnabled()) {
                continue;
            }

            // get recipes for specialty
            List<String[]> recipes = specialty.getRecipes();

            SpecialItems specialItems = new SpecialItems(plugin);

            for (String[] recipeString : recipes) {

                // item to be crafted
                ItemStack item = new ItemStack(specialty.getRecipeProduct(recipeString));

                // make special
                ItemStack specialItem = specialItems.createSpecialItem(item);

                // set specialty
                specialItems.setSpecialty(specialItem, specialty, true, specialty.getRecipeProperties(recipeString));

                ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, specialty.getRecipeName(recipeString)), specialItem);
                recipe.shape(recipeString[0], recipeString[1], recipeString[2]);

                HashMap<String, Material> key = specialty.getRecipeKey(recipeString);

                Set<String> ingredients = key.keySet();

                for (String material : ingredients) {
                    recipe.setIngredient(material.charAt(0), key.get(material));
                }

                Bukkit.addRecipe(recipe);
                registeredRecipes.add(recipe);
            }
        }
    }

    public static List<Recipe> getRegisteredRecipes() {
        return registeredRecipes;
    }
}
