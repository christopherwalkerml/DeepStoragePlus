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
                DeepStoragePlus.stashedDSU.put(player.getUniqueId(), event.getInventory());
                DeepStoragePlus.openDSU.put(player.getUniqueId(), (Container)event.getInventory().getLocation().getBlock().getState());
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
                    if (event.getClickedInventory() != player.getInventory()) {
                        if (event.getSlot() % 9 == 8) { //rightmost column
                            if (event.getSlot() != 53) { //if containers clicked
                                if (cursor != null && cursor.getType() != Material.AIR) { //if putting container in
                                    if (item.getType() == Material.WHITE_STAINED_GLASS_PANE) {
                                        event.setCancelled(true);
                                        if (cursor.hasItemMeta()) { //if putting a Storage Container in the DSU
                                            if (cursor.getItemMeta().getDisplayName().contains(LanguageManager.getValue("storagecontainer")) && cursor.getItemMeta().isUnbreakable()) {
                                                inv.setItem(event.getSlot(), cursor);
                                                cursor.setAmount(0);
                                                main.dsuupdatemanager.updateItems(inv);
                                            }
                                        }
                                    } else { //if trying to take placeholder out
                                        if (!(cursor.hasItemMeta() && cursor.getItemMeta().getDisplayName().contains(LanguageManager.getValue("storagecontainer")) && cursor.getItemMeta().isUnbreakable())) {
                                            event.setCancelled(true);
                                        } else if (event.isShiftClick()) {
                                            event.setCancelled(true);
                                        }
                                    }
                                } else { //if taking container out
                                    event.setCancelled(true);
                                    if (item.getType() != Material.WHITE_STAINED_GLASS_PANE) {
                                        player.setItemOnCursor(item.clone());
                                        inv.setItem(event.getSlot(), DSUManager.getEmptyBlock());
                                        main.dsuupdatemanager.updateItems(inv);
                                    }
                                }
                            } else { //if io is clicked
                                event.setCancelled(true);
                                if (cursor == null || cursor.getType() == Material.AIR) {
                                    if (item != null && item.hasItemMeta()) {
                                        if (item.getItemMeta().getDisplayName().contains(LanguageManager.getValue("dsuioconfig"))) { //BOTTOM RIGHT FOR SETTINGS
                                            player.openInventory(createIOInventory(inv));
                                            player.updateInventory();
                                        }
                                    }
                                }
                            }
                        } else if (event.getSlot() % 9 == 7) { //walls
                            event.setCancelled(true);
                        } else { //items
                            event.setCancelled(true);
                            if (cursor != null && cursor.getType() != Material.AIR) {
                                boolean isvaliditem = DSUManager.addToDSU(cursor, event.getClickedInventory(), player); //try to add item to DSU
                                main.dsuupdatemanager.updateItems(inv);
                                if (cursor.getAmount() > 0 && isvaliditem) {
                                    player.sendMessage(DeepStoragePlus.prefix + ChatColor.RED.toString() + LanguageManager.getValue("containersfull"));
                                }
                            } else if (cursor == null || cursor.getType() == Material.AIR) { //taking item out of DSU
                                if (event.getClick() != ClickType.DOUBLE_CLICK) {
                                    if (event.isShiftClick()) {
                                        if (player.getInventory().firstEmpty() != -1) {
                                            int amtTaken = DSUManager.takeItems(item.getType(), inv);
                                            player.getInventory().addItem(new ItemStack(item.getType(), amtTaken));
                                        } else {
                                            player.sendMessage(DeepStoragePlus.prefix + ChatColor.RED.toString() + LanguageManager.getValue("nomorespace"));
                                        }
                                    } else {
                                        int amtTaken = DSUManager.takeItems(item.getType(), inv);
                                        player.setItemOnCursor(new ItemStack(item.getType(), amtTaken));
                                    }
                                    main.dsuupdatemanager.updateItems(inv);
                                }
                            }
                        }
                    } else { //if click is in player inventory
                        if (event.isShiftClick()) {
                            if (item != null && item.getType() != Material.AIR) {
                                main.dsumanager.addItemToDSU(item, player);
                                event.setCancelled(true);
                            }
                        } else if (event.getClick() == ClickType.DOUBLE_CLICK) {
                            event.setCancelled(true);
                        }
                    }

                } else if (event.getView().getTitle().equals(ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + LanguageManager.getValue("dsuioconfig"))) {
                    event.setCancelled(true);
                    if (event.getSlot() == 8 || event.getSlot() == 17) {
                        startSelection(event.getSlot(), inv);
                    } else { //change selection and io items
                        if (event.getSlot() % 9 != 8 && event.getSlot() % 9 != 7) {
                            if (item != null) {
                                for (int i = 0; i < inv.getContents().length; i++) {
                                    if (inv.getItem(i) != null && inv.getItem(i).getEnchantments().size() > 0) {
                                        ItemStack newitem = item.clone();
                                        ItemMeta itemmeta = newitem.getItemMeta();
                                        if (i == 8) {
                                            itemmeta.setDisplayName(ChatColor.GRAY + LanguageManager.getValue("input") + ": " + ChatColor.GREEN + matToString(newitem.getType()));
                                        } else {
                                            itemmeta.setDisplayName(ChatColor.GRAY + LanguageManager.getValue("output") + ": " + ChatColor.GREEN + matToString(newitem.getType()));
                                        }
                                        itemmeta.setLore(Arrays.asList(ChatColor.GRAY + LanguageManager.getValue("clicktoclear")));
                                        newitem.setItemMeta(itemmeta);
                                        inv.setItem(i, newitem);
                                    }
                                }
                            }
                        } else { //change sorting types in io config
                            if (item != null && item.getType() == Material.COMPASS) {
                                if (event.getClick() != ClickType.DOUBLE_CLICK) {
                                    ItemMeta meta = item.getItemMeta();
                                    if (meta.getDisplayName().contains(LanguageManager.getValue("container"))) {
                                        meta.setDisplayName(meta.getDisplayName().replace(LanguageManager.getValue("container"), LanguageManager.getValue("alpha")));
                                    } else if (meta.getDisplayName().contains(LanguageManager.getValue("alpha"))) {
                                        meta.setDisplayName(meta.getDisplayName().replace(LanguageManager.getValue("alpha"), LanguageManager.getValue("amount")));
                                    } else if (meta.getDisplayName().contains(LanguageManager.getValue("amount"))) {
                                        meta.setDisplayName(meta.getDisplayName().replace(LanguageManager.getValue("amount"), "ID"));
                                    } else {
                                        meta.setDisplayName(meta.getDisplayName().replace("ID", LanguageManager.getValue("container")));
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
            if (event.getView().getTitle().equals(ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + LanguageManager.getValue("dsuioconfig"))) {
                Container DSUContainer = main.openDSU.get(player.getUniqueId());
                Inventory DSU = DSUContainer.getInventory();

                Inventory IOInv = event.getInventory();
                ItemStack input = IOInv.getItem(8);
                ItemStack output = IOInv.getItem(17);
                ItemStack sorting = IOInv.getItem(26);

                List<String> lore = new ArrayList<>();

                if (!input.getItemMeta().getDisplayName().contains(LanguageManager.getValue("all"))) {
                    lore.add(ChatColor.GRAY + LanguageManager.getValue("input") + ": " + ChatColor.GREEN + matToString(input.getType()));
                } else {
                    lore.add(ChatColor.GRAY + LanguageManager.getValue("input") + ": " + ChatColor.BLUE + LanguageManager.getValue("all"));
                }
                if (!output.getItemMeta().getDisplayName().contains(LanguageManager.getValue("none"))) {
                    lore.add(ChatColor.GRAY + LanguageManager.getValue("output") + ": " + ChatColor.GREEN + matToString(output.getType()));
                } else {
                    lore.add(ChatColor.GRAY + LanguageManager.getValue("output") + ": " + ChatColor.BLUE + LanguageManager.getValue("none"));
                }
                lore.add(sorting.getItemMeta().getDisplayName());

                ItemStack i = DSU.getItem(53);
                ItemMeta m = i.getItemMeta();
                m.setLore(lore);
                i.setItemMeta(m);
            } else if (event.getView().getTitle().equals(DeepStoragePlus.DSUname)) {
                DeepStoragePlus.stashedDSU.remove(player.getUniqueId());
            }
        }
    }
}
