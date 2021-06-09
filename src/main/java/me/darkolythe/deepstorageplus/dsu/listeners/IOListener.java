package me.darkolythe.deepstorageplus.dsu.listeners;

import me.darkolythe.deepstorageplus.DeepStoragePlus;
import me.darkolythe.deepstorageplus.dsu.StorageUtils;
import me.darkolythe.deepstorageplus.dsu.managers.DSUManager;
import me.darkolythe.deepstorageplus.dsu.managers.SorterManager;
import me.darkolythe.deepstorageplus.utils.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static me.darkolythe.deepstorageplus.dsu.StorageUtils.hasNoMeta;
import static me.darkolythe.deepstorageplus.dsu.StorageUtils.stringToMat;
import static me.darkolythe.deepstorageplus.dsu.managers.DSUManager.addDataToContainer;
import static me.darkolythe.deepstorageplus.dsu.managers.SettingsManager.addSpeedUpgrade;
import static me.darkolythe.deepstorageplus.dsu.managers.SettingsManager.getSpeedUpgrade;
import static me.darkolythe.deepstorageplus.utils.ItemList.createSpeedUpgrade;

public class IOListener implements Listener {

    private DeepStoragePlus main;
    public IOListener(DeepStoragePlus plugin) {
        this.main = plugin; // set it equal to an instance of main
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onDSUClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();
            if (block != null && block.getType() == Material.CHEST) {
                if (!event.isCancelled()) {
                    Chest chest = (Chest) block.getState();
                    Inventory inv = chest.getInventory();
                    if (chest.getInventory().contains(DSUManager.getDSUWall())) {
                        ItemStack item = player.getInventory().getItemInMainHand();
                        if (compareItemStacks(item, createSpeedUpgrade())) {
                            ItemStack IOItem = inv.getItem(53);
                            ItemStack newIOItem = addSpeedUpgrade(IOItem);
                            if (newIOItem != null) {
                                inv.setItem(53, newIOItem);
                                player.sendMessage(DeepStoragePlus.prefix + ChatColor.GREEN + LanguageManager.getValue("upgradesuccess"));
                                item.setAmount(item.getAmount() - 1);
                            } else {
                                player.sendMessage(DeepStoragePlus.prefix + ChatColor.RED + LanguageManager.getValue("upgradefail"));
                            }
                            inv.getLocation().getBlock().getState().update();
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onHopperInput(InventoryMoveItemEvent event) {
        Inventory initial = event.getSource();
        Inventory dest = event.getDestination();

        if (initial.getContents().length == 54 || dest.getContents().length == 54) {

            ItemStack moveItem = event.getItem();

            ItemStack IOSettings;
            Inventory IOInv;
            Material input;
            Material output;
            String IOStatus = "input";

            if (initial.getContents().length == 54) {
                IOSettings = initial.getItem(53);
                IOInv = initial;
                IOStatus = "output";
            } else {
                IOSettings = dest.getItem(53);
                IOInv = dest;
            }

            if (!(IOInv.getLocation().getBlock().getState() instanceof Chest)) {
                return;
            }

            if (StorageUtils.isDSU(IOInv)) {
                event.setCancelled(true);

                if (hasNoMeta(moveItem)) {
                    if (IOSettings != null) {
                        input = getInput(IOSettings);
                        output = getOutput(IOSettings);

                        int amt = getSpeedUpgrade(IOSettings);

                        if (IOStatus.equals("input")) {
                            lookForItemInHopper(initial, dest, input, amt + 1);
                            return;
                        } else {
                            lookForItemInChest(output, initial, dest, moveItem, amt + 1);
                            return;
                        }
                    }
                }
            } else if (StorageUtils.isSorter(IOInv)) {
                if (IOStatus.equals("input")) {
                    main.sorterUpdateManager.sortItems(IOInv, DeepStoragePlus.minTimeSinceLastSortHopper);
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

    private static Material getInput(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore.get(0).contains(LanguageManager.getValue("all"))) {
            return null;
        } else {
            return stringToMat(lore.get(0), ChatColor.GRAY + LanguageManager.getValue("input") + ": " + ChatColor.GREEN);
        }
    }

    private static Material getOutput(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore.get(1).contains(LanguageManager.getValue("none"))) {
            return null;
        } else {
            return stringToMat(lore.get(1), ChatColor.GRAY + LanguageManager.getValue("output") + ": " + ChatColor.GREEN);
        }
    }

    private void lookForItemInHopper(Inventory initial, Inventory dest, Material input, int amt) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
            @Override
            public void run() {
                boolean moved_stack = false;
                for (int i = 0; i < 5; i++) {
                    if (!moved_stack) {
                        ItemStack toMove = initial.getItem(i);
                        if (toMove != null && (input == null || input == toMove.getType())) {
                            ItemStack moving = toMove.clone();
                            moving.setAmount(Math.min(amt, toMove.getAmount()));
                            if (hasNoMeta(moving)) { //items being stored cannot have any special features. ie: damage, enchants, name, lore.
                                for (int j = 0; j < 5; j++) {
                                    if (moving.getAmount() > 0) { //if the item amount is greater than 0, it means there are still items to put in the containers
                                        addDataToContainer(dest.getItem(8 + (9 * j)), moving); //add the item to the current loop container
                                        toMove.setAmount(toMove.getAmount() - (amt - moving.getAmount()));

                                        main.dsuupdatemanager.updateItems(dest, input);
                                        moved_stack = true;
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }, 1);
    }

    private void lookForItemInChest(Material output, Inventory initial, Inventory dest, ItemStack moveItem, int amt) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
            @Override
            public void run() {

                if (initial.first(moveItem.getType()) != 0) {
                    return;
                }

                if (output != null) {
                    for (int i = 0; i < 5; i++) {
                        ItemStack container = initial.getItem(8 + (9 * i));
                        if (container.getType() != Material.WHITE_STAINED_GLASS_PANE) {
                            HashSet<Material> mats = DSUManager.getTypes(container.getItemMeta().getLore());

                            if (mats.contains(output)) {
                                HashMap<Integer, ItemStack> items = dest.addItem(new ItemStack(output, amt));
                                int sub = 0;
                                for (ItemStack overflow : items.values()) {
                                    sub += overflow.getAmount();
                                }
                                DSUManager.takeItems(output, initial, amt - sub);

                                main.dsuupdatemanager.updateItems(initial, output);
                                return;
                            }
                        }
                    }
                }
            }
        }, 1);
    }

    private static boolean compareItemStacks(ItemStack item1, ItemStack item2) {
        ItemStack i1 = item1.clone();
        ItemStack i2 = item2.clone();
        i1.setAmount(1);
        i2.setAmount(1);
        return i1.equals(i2);
    }
}
