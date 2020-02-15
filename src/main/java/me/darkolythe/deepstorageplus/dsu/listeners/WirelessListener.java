package me.darkolythe.deepstorageplus.dsu.listeners;

import me.darkolythe.deepstorageplus.DeepStoragePlus;
import me.darkolythe.deepstorageplus.utils.LanguageManager;
import me.darkolythe.deepstorageplus.utils.RecipeManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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
    }

    @EventHandler
    private void onDSUClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();
            if (block != null && block.getType() == Material.CHEST) {
                if (!event.isCancelled()) {
                    Chest chest = (Chest) block.getState();
                    if (chest.getCustomName() != null && chest.getCustomName().equals(DeepStoragePlus.DSUname)) {
                        if (player.getInventory().getItemInMainHand().equals(createTerminal())) {
                            event.setCancelled(true);
                            updateTerminal(player.getInventory().getItemInMainHand(), block.getX(), block.getY(), block.getZ(), block.getWorld());
                            return;
                        }
                    }
                }
            }
            if (block == null || !(block.getState() instanceof InventoryHolder)) {
                ItemStack hand = player.getInventory().getItemInMainHand();
                if (hand.hasItemMeta() && hand.getItemMeta().hasDisplayName() && hand.getItemMeta().getDisplayName().equals(createTerminal().getItemMeta().getDisplayName())) {
                    if (hand.getItemMeta().getLore().contains(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + LanguageManager.getValue("linked"))) {
                        Inventory dsu = getWirelessDSU(hand, player);
                        if (dsu != null) {
                            event.setCancelled(true);
                            player.openInventory(dsu);
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
                ItemMeta meta = item.getItemMeta();
                if (meta.getLore().get(0).equals(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + LanguageManager.getValue("linked"))) {
                    event.getPlayer().getInventory().setItemInMainHand(createTerminal());
                }
            }
        }
    }
}
