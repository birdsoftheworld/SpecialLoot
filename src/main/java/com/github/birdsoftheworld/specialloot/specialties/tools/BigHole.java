package com.github.birdsoftheworld.specialloot.specialties.tools;

import com.github.birdsoftheworld.specialloot.specialties.InteractSpecial;
import com.github.birdsoftheworld.specialloot.specialties.Specialty;
import com.github.birdsoftheworld.specialloot.util.SpecialtyProperties;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class BigHole extends Specialty implements InteractSpecial {
    public BigHole(Plugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onInteract(PlayerInteractEvent event, SpecialtyProperties properties) {
        // only clicks on blocks
        if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return false;
        }

        // only main hand
        EquipmentSlot hand = event.getHand();
        if (hand == null || hand.equals(EquipmentSlot.OFF_HAND)) {
            return false;
        }

        Block block = event.getClickedBlock();

        if (block == null || block.isPassable()) {
            return false;
        }

        BlockFace face = event.getBlockFace();
        BlockFace away = face.getOppositeFace();

        int range = (int) properties.getPropertyOrDefault("range", 1).getValue();

        Block middleBlock = block;

        for (int i = 0; i < range; i++) {
            middleBlock = middleBlock.getRelative(away);
        }

        destroySurrounding(middleBlock, range, event.getItem(), properties);

        Player player = event.getPlayer();
        player.playSound(player.getLocation(), Sound.BLOCK_STONE_BREAK, 1f, 1f);

        return true;
    }

    @Override
    public boolean onInteractEntity(PlayerInteractEntityEvent event, SpecialtyProperties properties) {
        return false;
    }

    private void destroySurrounding(Block block, int range, ItemStack item, SpecialtyProperties properties) {
        ArrayList blacklist = (ArrayList) properties.getProperty("blacklist").getValue();
        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    Block surroundingBlock = block.getRelative(x, y, z);
                    if (!surroundingBlock.isPassable()) {
                        if (!blacklist.contains(surroundingBlock.getType().name())) {
                            surroundingBlock.breakNaturally(item);
                        }
                    }
                }
            }
        }
    }
}
