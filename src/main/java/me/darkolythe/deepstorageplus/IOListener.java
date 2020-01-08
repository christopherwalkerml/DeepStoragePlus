package me.darkolythe.deepstorageplus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static me.darkolythe.deepstorageplus.DSUManager.addDataToContainer;
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

        if (initial.getContents().length == 54 || dest.getContents().length == 54) {

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
                        boolean hasAdded = false;
                        for (int i = 0; i < 5; i++) {
                            ItemStack toMove = initial.getItem(i);
                            if (toMove != null && (input == null || input == toMove.getType())) {
                                ItemStack toMoveItem = toMove.clone();
                                toMoveItem.setAmount(1);
                                ItemStack moveClone = toMoveItem.clone();
                                for (int j = 0; j < 5; j++) {
                                    if (toMoveItem.getAmount() > 0) {
                                        addDataToContainer(dest.getItem(8 + (9 * j)), toMoveItem); //add the item to the current loop container
                                    } else {
                                        hasAdded = true;
                                        dest.getLocation().getBlock().getState().update();
                                        removeItemFromHopper(moveClone, initial);
                                        break;
                                    }
                                }
                            }
                            if (hasAdded) {
                                break;
                            }
                        }
                    } else {

                    }
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
            return Material.valueOf(lore.get(0).replace(ChatColor.GRAY + "Input: " + ChatColor.GREEN, "").toUpperCase());
        }
    }

    private static Material getOutput(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore.get(1).contains("none")) {
            return null;
        } else {
            return Material.valueOf(lore.get(0).replace(ChatColor.GRAY + "Output: " + ChatColor.GREEN, "").toUpperCase());
        }
    }

    private void removeItemFromHopper(ItemStack item, Inventory inv) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < inv.getContents().length; i++) {
                    if (inv.getItem(i) != null) {
                        if (compareItemStacks(inv.getItem(i), item)) {
                            ItemStack tempItem = inv.getItem(i);
                            tempItem.setAmount(inv.getItem(i).getAmount() - 1);
                            inv.setItem(i, tempItem);
                            break;
                        }
                    }
                }
                inv.getLocation().getBlock().getState().update();
            }
        },1);
    }

    private static boolean compareItemStacks(ItemStack item1, ItemStack item2) {
        ItemStack i1 = item1.clone();
        ItemStack i2 = item2.clone();
        i1.setAmount(1);
        i2.setAmount(1);
        return i1.equals(i2);
    }
}
