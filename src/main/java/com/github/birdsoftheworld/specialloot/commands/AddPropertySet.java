package com.github.birdsoftheworld.specialloot.commands;

import com.github.birdsoftheworld.specialloot.specialties.Specialties;
import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import com.github.birdsoftheworld.specialloot.util.SpecialtyProperties;
import com.github.birdsoftheworld.specialloot.util.SpecialtyProperty;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AddPropertySet implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 4) {
            return false;
        }

        // odd args
        if (args.length % 2 == 1) {
            return false;
        }

        String specialtyName = args[0];

        Specialty specialty = Specialties.valueOf(specialtyName);

        if (specialty == null) {
            return false;
        }

        String set = args[1];

        SpecialtyProperties properties = new SpecialtyProperties();

        for (int i = 2; i < args.length; i += 2) {
            SpecialtyProperty<String> property = new SpecialtyProperty<>(args[i + 1]);
            properties.setProperty(args[i], property);
        }

        specialty.setProperties(set, properties);

        return true;
    }
}
