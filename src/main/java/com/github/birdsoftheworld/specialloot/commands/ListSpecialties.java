package com.github.birdsoftheworld.specialloot.commands;

import com.github.birdsoftheworld.specialloot.enums.Specialties;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ListSpecialties implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        for(Specialties specialty : Specialties.values()) {
            sender.sendMessage(ChatColor.BOLD.toString() + ChatColor.GREEN.toString() + specialty.name().toLowerCase());
            sender.sendMessage(specialty.getDescription());
        }
        return true;
    }
}
