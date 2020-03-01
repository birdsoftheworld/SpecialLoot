package com.github.birdsoftheworld.specialloot.commands;

import com.github.birdsoftheworld.specialloot.specialties.Specialties;
import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ListSpecialties implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        for(Specialty specialty : Specialties.values()) {
            sender.sendMessage(ChatColor.BOLD.toString() + ChatColor.GREEN.toString() + specialty.getName());
            sender.sendMessage((String) specialty.getDefaultProperties().getProperty("lore").getValue());
        }
        return true;
    }
}
