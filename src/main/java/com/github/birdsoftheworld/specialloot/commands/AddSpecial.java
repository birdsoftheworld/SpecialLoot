package com.github.birdsoftheworld.specialloot.commands;

import com.github.birdsoftheworld.specialloot.enums.Specials;
import com.github.birdsoftheworld.specialloot.util.SpecialItems;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class AddSpecial implements CommandExecutor {

    private Plugin plugin;

    public AddSpecial(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED.toString() + "You must be a player to use this command.");
            return true;
        }

        if(strings.length < 1) {
            return false;
        }

        Player player = (Player) commandSender;
        PlayerInventory playerInventory = player.getInventory();
        ItemStack heldItem = playerInventory.getItemInMainHand();

        Specials special;
        try {
            special = Specials.valueOf(strings[0].toUpperCase());
        } catch (Exception ignored) {
            player.sendMessage(ChatColor.RED.toString() + "That's not a special.");
            return true;
        }

        ItemStack specialItem = SpecialItems.createSpecial(heldItem, plugin, special, true);

        playerInventory.setItemInMainHand(specialItem);

        return true;
    }
}
