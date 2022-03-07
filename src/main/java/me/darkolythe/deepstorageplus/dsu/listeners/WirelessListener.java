package me.darkolythe.deepstorageplus.dsu.listeners;

import me.darkolythe.deepstorageplus.DeepStoragePlus;
import me.darkolythe.deepstorageplus.dsu.managers.DSUManager;
import me.darkolythe.deepstorageplus.utils.LanguageManager;
import me.darkolythe.deepstorageplus.utils.RecipeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

import static me.darkolythe.deepstorageplus.dsu.managers.WirelessManager.*;


public class WirelessListener implements Listener {

    private DeepStoragePlus main;
    public WirelessListener(DeepStoragePlus plugin) {
        this.main = plugin; // set it equal to an instance of main
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onDSUClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();
            if (block != null && block.getType() == Material.CHEST) {
                if (!event.isCancelled()) {
                    Chest chest = (Chest) block.getState();
                    if (chest.getInventory().contains(DSUManager.getDSUWall())) {
                        ItemStack item = player.getInventory().getItemInMainHand();
                        if (isWirelessTerminal(item)) {
                            if (item.getItemMeta().getLore().contains(ChatColor.RED.toString() + ChatColor.BOLD.toString() + LanguageManager.getValue("unlinked"))) {
                                event.setCancelled(true);
                                updateTerminal(item, block.getX(), block.getY(), block.getZ(), block.getWorld());
                                return;
                            }
                        }
                    }
                }
            }
            if (block == null || !(block.getState() instanceof InventoryHolder)) {
                ItemStack hand = player.getInventory().getItemInMainHand();
                if (isWirelessTerminal(hand)) {
                    if (hand.getItemMeta().getLore().contains(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + LanguageManager.getValue("linked"))) {
                        Inventory dsu = getWirelessDSU(hand, player);
                        if (dsu != null) {
                            if (player.hasPermission("deepstorageplus.wireless")) {
                                event.setCancelled(true);
                                player.openInventory(dsu);
                                Chunk c = dsu.getLocation().getChunk();
                                c.setForceLoaded(true);
                                DeepStoragePlus.loadedChunks.put(player, c);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    private void onTerminalSwap(PlayerSwapHandItemsEvent event) {
        ItemStack item = event.getOffHandItem();
        if (event.getPlayer().isSneaking()) {
            if (isWirelessTerminal(item)) {
                if (item.getItemMeta().getLore().get(0).equals(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + LanguageManager.getValue("linked"))) {
                    String name = item.getItemMeta().getDisplayName();
                    ItemStack newitem = createTerminal();
                    ItemMeta meta = newitem.getItemMeta();
                    meta.setDisplayName(name);
                    newitem.setItemMeta(meta);
                    event.getPlayer().getInventory().setItemInMainHand(newitem);

                    event.setCancelled(true);
                }
            }
        }
    }
}
