package me.darkolythe.deepstorageplus;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.darkolythe.deepstorageplus.SettingsManager.createIOInventory;
import static me.darkolythe.deepstorageplus.SettingsManager.startSelection;
import static me.darkolythe.deepstorageplus.StorageUtils.matToString;

class InventoryListener implements Listener {

    private DeepStoragePlus main;
    InventoryListener(DeepStoragePlus plugin) {
        this.main = plugin; // set it equal to an instance of main
    }

    @EventHandler
    private void onStorageOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if (event.getView().getTitle().equals(DeepStoragePlus.DSUname)) {
                main.openDSU.put(player.getUniqueId(), (Container)event.getInventory().getLocation().getBlock().getState());
                DSUManager.verifyInventory(event.getInventory());
                main.dsuupdatemanager.updateItems(event.getInventory());
            }
        }
    }

    @EventHandler
    private void onStorageInteract(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();
                Inventory inv = event.getInventory();
                ItemStack item = event.getCurrentItem();
                ItemStack cursor = event.getCursor();
                if (event.getView().getTitle().equals(DeepStoragePlus.DSUname)) {
                    if (item != null && item.hasItemMeta()) {
                        if (event.getClickedInventory() != player.getInventory()) {
                            if (item.getItemMeta().getDisplayName().equals(ChatColor.DARK_GRAY + "DSU Walls")) { //DSU WALLS
                                event.setCancelled(true);
                            } else if (event.getSlot() % 9 == 8 && event.getSlot() != 53) { //RIGHT BAR FOR STORAGE CELLS
                                if (item.getType() == Material.WHITE_STAINED_GLASS_PANE) {
                                    event.setCancelled(true);
                                    if (cursor != null && cursor.hasItemMeta()) { //if putting a Storage Container in the DSU
                                        if (cursor.getItemMeta().getDisplayName().contains("Storage Container") && cursor.getItemMeta().isUnbreakable()) {
                                            inv.setItem(event.getSlot(), cursor);
                                            cursor.setAmount(0);
                                            main.dsuupdatemanager.updateItems(inv);
                                        }
                                    }
                                } else if (cursor != null && cursor.getType() != Material.AIR) {
                                    if (!(cursor.hasItemMeta() && cursor.getItemMeta().getDisplayName().contains("Storage Container") && cursor.getItemMeta().isUnbreakable())) {
                                        event.setCancelled(true);
                                    } else if (event.isShiftClick()) {
                                        event.setCancelled(true);
                                    }
                                } else if (cursor == null || cursor.getType() == Material.AIR) { //if taking a Storage Container out of the DSU
                                    event.setCancelled(true);
                                    player.setItemOnCursor(item.clone());
                                    inv.setItem(event.getSlot(), DSUManager.getEmptyBlock());
                                    main.dsuupdatemanager.updateItems(inv);
                                }
                            } else if (item.getItemMeta().getDisplayName().contains("DSU IO Configuration")) { //BOTTOM RIGHT FOR SETTINGS
                                event.setCancelled(true);
                                player.openInventory(createIOInventory(inv));
                                player.updateInventory();
                            }
                        } else if (event.getClickedInventory() == player.getInventory()) {
                            if (event.isShiftClick()) {
                                if (item.getType() != Material.AIR) {
                                    main.dsumanager.addItemToDSU(item, player);
                                    event.setCancelled(true);
                                }
                            }
                        }
                    } else if (event.getClickedInventory() == player.getInventory()) {
                        if (event.isShiftClick()) {
                            if (item != null && item.getType() != Material.AIR) {
                                main.dsumanager.addItemToDSU(item, player);
                                event.setCancelled(true);
                            }
                        }
                    } else { //if click in DSU with item on cursor
                        event.setCancelled(true);
                        if (cursor != null && cursor.getType() != Material.AIR) {
                            DSUManager.addToDSU(cursor, event.getClickedInventory(), player); //try to add item to DSU
                            main.dsuupdatemanager.updateItems(inv);
                            if (cursor.getAmount() > 0) {
                                player.sendMessage(DeepStoragePlus.prefix + ChatColor.RED.toString() + "Storage containers are full");
                            }
                        } else if (cursor == null || cursor.getType() == Material.AIR) {
                            if (event.getClick() != ClickType.DOUBLE_CLICK) {
                                int amtTaken = DSUManager.takeItems(item.getType(), inv);
                                if (event.isShiftClick()) {
                                    if (player.getInventory().firstEmpty() != -1) {
                                        player.getInventory().addItem(new ItemStack(item.getType(), amtTaken));
                                    } else {
                                        player.sendMessage(DeepStoragePlus.prefix + ChatColor.RED.toString() + "No more space for items in your inventory");
                                    }
                                } else {
                                    player.setItemOnCursor(new ItemStack(item.getType(), amtTaken));
                                }
                                main.dsuupdatemanager.updateItems(inv);
                            }
                        }
                    }
                } else if (event.getView().getTitle().equals(ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + "DSU IO Configuration")) {
                    event.setCancelled(true);
                    if (event.getSlot() == 8 || event.getSlot() == 17) {
                        startSelection(event.getSlot(), inv);
                    } else {
                        if (event.getSlot() % 9 != 8 && event.getSlot() % 9 != 7) {
                            if (item != null) {
                                for (int i = 0; i < inv.getContents().length; i++) {
                                    if (inv.getItem(i) != null && inv.getItem(i).getEnchantments().size() > 0) {
                                        ItemStack newitem = item.clone();
                                        ItemMeta itemmeta = newitem.getItemMeta();
                                        if (i == 8) {
                                            itemmeta.setDisplayName(ChatColor.GRAY + "Input: " + ChatColor.GREEN + matToString(newitem.getType()));
                                        } else {
                                            itemmeta.setDisplayName(ChatColor.GRAY + "Output: " + ChatColor.GREEN + matToString(newitem.getType()));
                                        }
                                        itemmeta.setLore(Arrays.asList(ChatColor.GRAY + "Click on this slot to clear selection."));
                                        newitem.setItemMeta(itemmeta);
                                        inv.setItem(i, newitem);
                                    }
                                }
                            }
                        } else {
                            if (item != null && item.getType() == Material.COMPASS) {
                                if (event.getClick() != ClickType.DOUBLE_CLICK) {
                                    ItemMeta meta = item.getItemMeta();
                                    if (meta.getDisplayName().contains("container")) {
                                        meta.setDisplayName(meta.getDisplayName().replace("container", "alpha"));
                                    } else if (meta.getDisplayName().contains("alpha")) {
                                        meta.setDisplayName(meta.getDisplayName().replace("alpha", "amount"));
                                    } else if (meta.getDisplayName().contains("amount")) {
                                        meta.setDisplayName(meta.getDisplayName().replace("amount", "ID"));
                                    } else {
                                        meta.setDisplayName(meta.getDisplayName().replace("ID", "container"));
                                    }
                                    item.setItemMeta(meta);
                                    inv.setItem(event.getSlot(), item);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    private void onInventoryDrag(InventoryDragEvent event) {
        if (event.getView().getTitle().equals(DeepStoragePlus.DSUname)) {
            if (event.getWhoClicked() instanceof Player) {
                for (Integer slot : event.getRawSlots()) {
                    if (slot <= 51) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    private void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if (event.getView().getTitle().equals(ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + "DSU IO Configuration")) {
                Container DSUContainer = main.openDSU.get(player.getUniqueId());
                Inventory DSU = DSUContainer.getInventory();
                if (DSU != null) {
                    Inventory IOInv = event.getInventory();
                    ItemStack input = IOInv.getItem(8);
                    ItemStack output = IOInv.getItem(17);
                    ItemStack sorting = IOInv.getItem(26);

                    List<String> lore = new ArrayList<>();

                    if (!input.getItemMeta().getDisplayName().contains("all")) {
                        lore.add(ChatColor.GRAY + "Input: " + ChatColor.GREEN + matToString(input.getType()));
                    } else {
                        lore.add(ChatColor.GRAY + "Input: " + ChatColor.BLUE + "all");
                    }
                    if (!output.getItemMeta().getDisplayName().contains("none")) {
                        lore.add(ChatColor.GRAY + "Output: " + ChatColor.GREEN + matToString(output.getType()));
                    } else {
                        lore.add(ChatColor.GRAY + "Output: " + ChatColor.BLUE + "none");
                    }
                    lore.add(sorting.getItemMeta().getDisplayName());

                    ItemStack i = DSU.getItem(53);
                    ItemMeta m = i.getItemMeta();
                    m.setLore(lore);
                    i.setItemMeta(m);
                }
            }
        }
    }
}
