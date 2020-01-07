package me.darkolythe.deepstorageplus;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static me.darkolythe.deepstorageplus.DSUManager.addDataToContainer;
import static me.darkolythe.deepstorageplus.DSUManager.addItemToDSU;
import static me.darkolythe.deepstorageplus.StorageUtils.hasNoMeta;

public class IOListener implements Listener {

    private DeepStoragePlus main;
    public IOListener(DeepStoragePlus plugin) {
        this.main = plugin; // set it equal to an instance of main
    }

    @EventHandler
    private void onHopperInput(InventoryMoveItemEvent event) {
        Inventory initial = event.getInitiator();
        Inventory dest = event.getDestination();

        ItemStack moveItem = event.getItem();

        ItemStack IOSettings = null;

        Material input = null;
        Material output = null;

        String IOStatus = "input";

        if (initial.getContents().length == 54) {
            IOSettings = initial.getItem(53);
            IOStatus = "output";
        } else {
            IOSettings = dest.getItem(53);
        }

        if (IOSettings != null && IOSettings.hasItemMeta() && IOSettings.getItemMeta().hasDisplayName() && IOSettings.getItemMeta().getDisplayName().equals("DSU IO Configuration")) {
            event.setCancelled(true);
            if (hasNoMeta(moveItem)) {
                input = getInput(IOSettings);
                output = getOutput(IOSettings);

                if (IOStatus.equals("input")) {
                    if (input == null || input == moveItem.getType()) {
                        if (hasNoMeta(moveItem)) { //items being stored cannot have any special features. ie: damage, enchants, name, lore.
                            for (int i = 0; i < 5; i++) {
                                if (moveItem.getAmount() > 0) { //if the item amount is greater than 0, it means there are still items to put in the containers
                                    addDataToContainer(dest.getItem(8 + (9 * i)), moveItem); //add the item to the current loop container
                                } else {
                                    return;
                                }
                            }
                        }
                    }
                } else {

                }
            }
        }
    }

    private static Material getInput(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore.get(0).contains("all")) {
            return null;
        } else {
            return Material.valueOf(lore.get(0).replace(ChatColor.GRAY + "Input: " + ChatColor.GREEN, ""));
        }
    }

    private static Material getOutput(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore.get(1).contains("none")) {
            return null;
        } else {
            return Material.valueOf(lore.get(0).replace(ChatColor.GRAY + "Output: " + ChatColor.GREEN, ""));
        }
    }
}
