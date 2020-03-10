package com.github.birdsoftheworld.specialloot.commands;

import com.github.birdsoftheworld.specialloot.specialties.Specialties;
import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import com.github.birdsoftheworld.specialloot.util.SpecialItems;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class AddSpecialty implements TabExecutor {

    private final Plugin plugin;
    private final SpecialItems specialItems;
    private final List<String> specialties;

    public AddSpecialty(Plugin plugin) {
        this.plugin = plugin;
        this.specialItems = new SpecialItems(plugin);

        specialties = new ArrayList<>();
        for (Specialty specialty : Specialties.values()) {
            specialties.add(specialty.getName());
        }
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED.toString() + "You must be a player to use this command.");
            return true;
        }

        // require arguments
        if(strings.length < 1 || strings.length > 2) {
            return false;
        }

        Player player = (Player) commandSender;
        PlayerInventory playerInventory = player.getInventory();
        ItemStack heldItem = playerInventory.getItemInMainHand();

        if (heldItem.getType().isAir()) {
            commandSender.sendMessage(ChatColor.RED.toString() + "You must be holding an item to use this command.");
            return true;
        }

        // get corresponding specialty
        Specialty specialty = Specialties.valueOf(strings[0].toUpperCase());
        if (specialty == null) {
            player.sendMessage(ChatColor.RED.toString() + "That's not a specialty.");
            return true;
        }

        ItemStack specialItem;
        // make item a special item if it isn't already
        if(specialItems.isSpecialItem(heldItem)) {
            specialItem = heldItem;
        } else {
            specialItem = specialItems.createSpecialItem(heldItem);
        }

        String propertySet = null;
        if (strings.length == 2) {
            propertySet = strings[1];
        }

        specialItems.setSpecialty(specialItem, specialty, true, propertySet);

        specialItems.makeUnstackable(specialItem);

        playerInventory.setItemInMainHand(specialItem);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> possibleTabCompletions = new ArrayList<>();
        if (args.length > 0) {
            for (final String possibleTabCompletion : specialties) {
                if (possibleTabCompletion.toLowerCase().contains(args[0].toLowerCase())) {
                    possibleTabCompletions.add(possibleTabCompletion);
                }
            }
        }

        return possibleTabCompletions;
    }
}
