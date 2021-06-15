package ru.starrysparklez.enchantmentmagic;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MagicEvents implements Listener {
    Main plugin;
    Logger logger;
    FileConfiguration config;

    public MagicEvents(Main plugin) {
        this.plugin = plugin;
        this.logger = plugin.logger;
        this.config = plugin.getConfig();
    }


    @EventHandler
    public void onFireBlockPlaced(BlockPlaceEvent event) {
        Block placedBlock = event.getBlockPlaced();
        if (placedBlock.getType() != Material.FIRE) return;

        Location blockLoc = placedBlock.getLocation();
        Entity[] entities = blockLoc.getChunk().getEntities();

        List<ItemStack> items = new ArrayList<>();
        Entity enchantedBookEnt = null;

        for (Entity ent : entities) {
            if (ent.getLocation().getBlockX() == blockLoc.getBlockX()
                    && ent.getLocation().getBlockY() == blockLoc.getBlockY()
                    && ent.getLocation().getBlockZ() == blockLoc.getBlockZ()
                    && ent.getType() == EntityType.DROPPED_ITEM) {
                ItemStack item = ((Item) ent).getItemStack();
                items.add(item);
                if (item.getType() == Material.ENCHANTED_BOOK) enchantedBookEnt = ent;
            }
        }

        if (new Location(
                blockLoc.getWorld(),
                blockLoc.getBlockX(),
                blockLoc.getBlockY() - 1,
                blockLoc.getBlockZ()
            ).getBlock().getType() != Material.OBSIDIAN
            || new Location(
                    blockLoc.getWorld(),
                    blockLoc.getBlockX(),
                    blockLoc.getBlockY() - 2,
                    blockLoc.getBlockZ()
            ).getBlock().getType() != Material.GOLD_BLOCK
            || new Location(
                    blockLoc.getWorld(),
                    blockLoc.getBlockX() - 1,
                    blockLoc.getBlockY() - 2,
                    blockLoc.getBlockZ()
            ).getBlock().getType() != Material.DIAMOND_BLOCK
            || new Location(
                    blockLoc.getWorld(),
                    blockLoc.getBlockX() + 1,
                    blockLoc.getBlockY() - 2,
                    blockLoc.getBlockZ()
            ).getBlock().getType() != Material.DIAMOND_BLOCK
            || new Location(
                    blockLoc.getWorld(),
                    blockLoc.getBlockX(),
                    blockLoc.getBlockY() - 2,
                    blockLoc.getBlockZ() - 1
            ).getBlock().getType() != Material.DIAMOND_BLOCK
            || new Location(
                    blockLoc.getWorld(),
                    blockLoc.getBlockX(),
                    blockLoc.getBlockY() - 2,
                    blockLoc.getBlockZ() + 1
            ).getBlock().getType() != Material.DIAMOND_BLOCK
            || items.size() != 2
        ) {
            return;
        };

        ItemStack enchantedBook = null;

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getType() == Material.ENCHANTED_BOOK) {
                enchantedBook = items.get(i);
                items.remove(i);
            };
        }

        if (enchantedBook == null) return;
        enchantedBookEnt.remove();

        event.setCancelled(true);
        blockLoc.getWorld().strikeLightningEffect(blockLoc);

        ItemStack targetItem = items.get(0);

        ((EnchantmentStorageMeta) enchantedBook.getItemMeta()).getStoredEnchants().forEach((e, lv) -> {
            targetItem.addUnsafeEnchantment(e, lv);
        });
    }
}