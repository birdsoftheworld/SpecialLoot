package com.github.birdsoftheworld.specialloot.listeners;

import com.github.birdsoftheworld.specialloot.specialties.ShieldSpecial;
import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import com.github.birdsoftheworld.specialloot.util.SpecialItems;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class ShieldListener extends SpecialtyListener implements Listener {

    private Plugin plugin;
    private SpecialItems specialItems;

    public ShieldListener(Plugin plugin) {
        this.plugin = plugin;
        this.specialItems = new SpecialItems(plugin);
    }

    @EventHandler
    public void onShieldBlock(EntityDamageByEntityEvent event) {
        Entity victim = event.getEntity();
        if (!(victim instanceof HumanEntity)) {
            return;
        }

        HumanEntity human = (HumanEntity) victim;
        if (!human.isBlocking()) {
            return;
        }

        ItemStack mainHand = human.getInventory().getItemInMainHand();
        ItemStack offHand = human.getInventory().getItemInOffHand();

        ItemStack shield = mainHand;

        if (!specialItems.isSpecialItem(mainHand)) {
            shield = offHand;
        }

        if (!specialItems.isSpecialItem(shield)) {
            return;
        }

        List<Specialty> specialties = specialItems.getSpecialties(shield);
        for (Specialty specialty : specialties) {
            // run specialty actions
            if (specialty instanceof ShieldSpecial) {
                boolean useDurability = ((ShieldSpecial) specialty).onShieldBlock(event);

                if (useDurability) {
                    Player player = (Player) human;
                    use(shield, player, specialItems, specialty);
                }
            }
        }
    }
}
