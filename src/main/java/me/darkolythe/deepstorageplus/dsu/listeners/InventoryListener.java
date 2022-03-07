package me.darkolythe.deepstorageplus.dsu.listeners;

import me.darkolythe.deepstorageplus.DeepStoragePlus;
import me.darkolythe.deepstorageplus.dsu.StorageUtils;
import me.darkolythe.deepstorageplus.dsu.managers.DSUManager;
import me.darkolythe.deepstorageplus.dsu.managers.SorterManager;
import me.darkolythe.deepstorageplus.utils.ItemList;
import me.darkolythe.deepstorageplus.utils.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static me.darkolythe.deepstorageplus.dsu.StorageUtils.matToString;
import static me.darkolythe.deepstorageplus.dsu.managers.SettingsManager.*;

public class InventoryListener implements Listener {

    private DeepStoragePlus main;
    public InventoryListener(DeepStoragePlus plugin) {
        this.main = plugin; // set it equal to an instance of main
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    private void onStorageOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if (event.getInventory().getSize() == 54) {
                if (event.getView().getTitle().equals(DeepStoragePlus.DSUname) || StorageUtils.isDSU(event.getInventory())) {
                    ItemStack lock = event.getInventory().getItem(53);
                    boolean isOp = player.hasPermission("deepstorageplus.adminopen");
                    boolean isLocked = isLocked(lock);
                    boolean canOpen = getLocked(lock, player);
                    if (canOpen || isOp || !isLocked) {
                        DeepStoragePlus.stashedDSU.put(player.getUniqueId(), event.getInventory());
                        DeepStoragePlus.openDSU.put(player.getUniqueId(), (Container) event.getInventory().getLocation().getBlock().getState());
                        DSUManager.verifyInventory(event.getInventory(), player);
                        main.dsuupdatemanager.updateItems(event.getInventory(), null);
                        return;
                    }
                    player.sendMessage(DeepStoragePlus.prefix + ChatColor.RED + LanguageManager.getValue("notallowedtoopen"));
                    event.setCancelled(true);
                }
                else if (event.getView().getTitle().equals(DeepStoragePlus.sortername) || StorageUtils.isDSU(event.getInventory())) {
                    SorterManager.verifyInventory(event.getInventory(), player);
                    main.sorterUpdateManager.sortItems(event.getInventory(), DeepStoragePlus.minTimeSinceLastSortPlayer);
                }
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

                if (event.getView().getTitle().equals(DeepStoragePlus.DSUname) || StorageUtils.isDSU(inv)) {
                    if (event.getClickedInventory() != player.getInventory()) {
                        if (event.getSlot() % 9 == 8) { //rightmost column
                            if (event.getSlot() != 53) { //if containers clicked
                                if (cursor != null && cursor.getType() != Material.AIR) { //if putting container in
                                    if (ItemList.isDSPItem(item, ItemList.STORAGE_SLOT_MODEL_ID)) {
                                        event.setCancelled(true);
                                        if (cursor.hasItemMeta()) { //if putting a Storage Container in the dsu
                                            System.out.println("meta check passed");
                                            if (ItemList.isStorageContainerItem(cursor)) {
                                                System.out.println("cursor is container");
                                                inv.setItem(event.getSlot(), cursor);
                                                cursor.setAmount(0);
                                                main.dsuupdatemanager.updateItems(inv, null);
                                            }
                                        }
                                    } else { //if trying to take placeholder out
                                        if (!ItemList.isStorageContainerItem(cursor)) {
                                            event.setCancelled(true);
                                        } else if (event.isShiftClick()) {
                                            event.setCancelled(true);
                                        }
                                    }
                                } else { //if taking container out
                                    event.setCancelled(true);
                                    if (ItemList.isStorageContainerItem(item)) {
                                        player.setItemOnCursor(item.clone());
                                        inv.setItem(event.getSlot(), DSUManager.getEmptyBlock());
                                        main.dsuupdatemanager.updateItems(inv, null);
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
                                Material mat = cursor.getType();
                                boolean isvaliditem = DSUManager.addToDSU(cursor, event.getClickedInventory(), player); //try to add item to dsu

                                main.dsuupdatemanager.updateItems(inv, mat);

                                if (cursor.getAmount() > 0 && isvaliditem) {
                                    player.sendMessage(DeepStoragePlus.prefix + ChatColor.RED + LanguageManager.getValue("containersfull"));
                                }
                            } else if (cursor == null || cursor.getType() == Material.AIR && item != null) { //taking item out of dsu
                                if (event.getClick() != ClickType.DOUBLE_CLICK) {
                                    Material mat = item.getType();
                                    if (event.isShiftClick()) {
                                        if (player.getInventory().firstEmpty() != -1) {
                                            int amtTaken = DSUManager.takeItems(item.getType(), inv, item.getType().getMaxStackSize());
                                            player.getInventory().addItem(new ItemStack(item.getType(), amtTaken));
                                        } else {
                                            player.sendMessage(DeepStoragePlus.prefix + ChatColor.RED + LanguageManager.getValue("nomorespace"));
                                        }
                                    } else {
                                        int amtTaken = DSUManager.takeItems(item.getType(), inv, item.getType().getMaxStackSize());
                                        player.setItemOnCursor(new ItemStack(item.getType(), amtTaken));
                                    }

                                    main.dsuupdatemanager.updateItems(inv, mat);
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

                } else if (event.getView().getTitle().equals(DeepStoragePlus.sortername) || StorageUtils.isSorter(inv)) {
                    if (event.getClickedInventory() != player.getInventory()) {
                        if (event.getSlot() > 26) { // link module field
                            if (cursor != null && cursor.getType() != Material.AIR) { //if putting container in
                                if (ItemList.isDSPItem(item, ItemList.LINK_SLOT_MODEL_ID)) {
                                    event.setCancelled(true);
                                    //if putting a link module into the sorter
                                    if (ItemList.isDSPItem(cursor, ItemList.LINK_MODULE_MODEL_ID)) {
                                        inv.setItem(event.getSlot(), cursor);
                                        cursor.setAmount(0);
                                    }
                                } else { //if trying to take placeholder out
                                    if (!ItemList.isDSPItem(cursor, ItemList.LINK_MODULE_MODEL_ID)) {
                                        event.setCancelled(true);
                                    } else if (event.isShiftClick()) {
                                        event.setCancelled(true);
                                    }
                                }
                            } else { //if taking link module out
                                event.setCancelled(true);
                                if (ItemList.isDSPItem(item, ItemList.LINK_MODULE_MODEL_ID)) {
                                    player.setItemOnCursor(item.clone());
                                    inv.setItem(event.getSlot(), SorterManager.getEmptyBlock());
                                }
                            }
                        } else if (event.getSlot() > 17 && event.getSlot() < 27) { //walls
                            event.setCancelled(true);
                        } else { //items
                            if (cursor != null && cursor.getType() != Material.AIR) { //putting an item into the sorter
                                main.sorterUpdateManager.sortItems(inv, DeepStoragePlus.minTimeSinceLastSortPlayer);
                            }
                        }
                    }
                    else { //if click is in player inventory
                        if (event.isShiftClick()) {
                            if (item != null && item.getType() != Material.AIR) {
                                main.sorterUpdateManager.sortItems(inv, DeepStoragePlus.minTimeSinceLastSortPlayer);
                            }
                        }
                    }

                } else if (event.getView().getTitle().equals(ChatColor.BLUE + ChatColor.BOLD.toString() + LanguageManager.getValue("dsuioconfig"))) {
                    event.setCancelled(true);
                    if (event.getSlot() == 8 || event.getSlot() == 17) {
                        startSelection(event.getSlot(), inv);
                    } else { //change selection and io items
                        if (event.getSlot() % 9 != 8 && event.getSlot() % 9 != 7) {
                            if (item != null) {
                                for (int i = 0; i < inv.getContents().length; i++) {
                                    ItemStack temp = inv.getItem(i);
                                    if (temp != null && temp.getEnchantments().size() > 0) {
                                        //If the item clicked is one of the DSU items to choose an IO item
                                        ItemStack newitem = item.clone();
                                        ItemMeta itemmeta = newitem.getItemMeta();
                                        if (i == 8) {
                                            itemmeta.setDisplayName(ChatColor.GRAY + LanguageManager.getValue("input") + ": " + ChatColor.GREEN + matToString(newitem.getType()));
                                        } else {
                                            itemmeta.setDisplayName(ChatColor.GRAY + LanguageManager.getValue("output") + ": " + ChatColor.GREEN + matToString(newitem.getType()));
                                        }
                                        itemmeta.setLore(Collections.singletonList(ChatColor.GRAY + LanguageManager.getValue("clicktoclear")));
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
                            } else if (item != null && item.getType() == Material.TRIPWIRE_HOOK) {
                                boolean isOwner = player.getUniqueId().toString().equals(getOwner(item)[1]);
                                boolean isOp = player.hasPermission("deepstorageplus.adminopen");
                                if (isOwner || isOp) { //only the owner or admin can edit the lock settings
                                    if (event.getClick() == ClickType.RIGHT) {
                                        ItemMeta meta = item.getItemMeta();
                                        meta.setLore(Arrays.asList(
                                                ChatColor.GRAY + LanguageManager.getValue("leftclicktoadd"),
                                                ChatColor.GRAY + LanguageManager.getValue("rightclicktoremove"),
                                                "",
                                                ChatColor.GRAY + LanguageManager.getValue("owner") + ": " + ChatColor.BLUE + getOwner(item)[0],
                                                ChatColor.GREEN + LanguageManager.getValue("unlocked")));
                                        item.setItemMeta(meta);
                                    } else if (event.getClick() == ClickType.LEFT) {
                                        player.sendMessage(DeepStoragePlus.prefix + ChatColor.GRAY + LanguageManager.getValue("entername"));
                                        player.sendMessage(ChatColor.GRAY + LanguageManager.getValue("typecancel"));
                                        DeepStoragePlus.stashedIO.put(player.getUniqueId(), inv);
                                        DeepStoragePlus.gettingInput.put(player.getUniqueId(), true);
                                        player.closeInventory();
                                    }
                                } else {
                                    player.sendMessage(DeepStoragePlus.prefix + ChatColor.GRAY + LanguageManager.getValue("notowner"));
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
        if (event.getView().getTitle().equals(DeepStoragePlus.DSUname) || StorageUtils.isDSU(event.getInventory())) {
            if (event.getWhoClicked() instanceof Player) {
                for (Integer slot : event.getRawSlots()) {
                    if (slot <= 51) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    /*
     * Update DSU with IO settings when closing IO settings menu
     */
    @EventHandler
    private void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if (event.getView().getTitle().equals(ChatColor.BLUE + ChatColor.BOLD.toString() + LanguageManager.getValue("dsuioconfig"))) {
                Container dsuContainer = DeepStoragePlus.openDSU.get(player.getUniqueId());
                Inventory dsu = dsuContainer.getInventory();

                Inventory IOInv = event.getInventory();
                ItemStack input = IOInv.getItem(8);
                ItemStack output = IOInv.getItem(17);
                ItemStack sorting = IOInv.getItem(26);
                ItemStack lock = IOInv.getItem(53);

                int speedUpgrade = getSpeedUpgrade(dsu.getItem(53));

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

                lore.add(ChatColor.GRAY + LanguageManager.getValue("iospeed") + ": " + ChatColor.GREEN + "+" + speedUpgrade);

                lore.add(ChatColor.GRAY + LanguageManager.getValue("owner") + ": " + ChatColor.BLUE + getOwner(lock)[0]);

                List<String> locklore = lock.getItemMeta().getLore();
                if (locklore.contains(ChatColor.RED + LanguageManager.getValue("locked"))) {
                    lore.add(ChatColor.RED + LanguageManager.getValue("locked"));
                    for (String s : getLockedUsers(lock)) {
                        lore.add(ChatColor.WHITE + s);
                    }
                } else {
                    lore.add(ChatColor.GREEN + LanguageManager.getValue("unlocked"));
                }

                ItemStack i = dsu.getItem(53);
                ItemMeta m = i.getItemMeta();
                m.setLore(lore);
                i.setItemMeta(m);

                // Open the DSU's main inventory after the player closes the settings menu
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
                    @Override
                    public void run() {
                        if (DeepStoragePlus.gettingInput.containsKey(player.getUniqueId()) && !DeepStoragePlus.gettingInput.get(player.getUniqueId())) {
                            player.openInventory(dsu);
                        }
                    }
                }, 1L);
            } else if (event.getView().getTitle().equals(DeepStoragePlus.DSUname) || StorageUtils.isDSU(event.getInventory())) {
                DeepStoragePlus.stashedDSU.remove(player.getUniqueId());
                if (DeepStoragePlus.loadedChunks.containsKey(player)) {
                    Chunk c = DeepStoragePlus.loadedChunks.get(player);
                    c.unload();
                    DeepStoragePlus.loadedChunks.remove(player);
                }
            } else if (event.getView().getTitle().equals(DeepStoragePlus.sortername) || StorageUtils.isSorter(event.getInventory())) {
                main.sorterUpdateManager.sortItems(event.getInventory(), DeepStoragePlus.minTimeSinceLastSortPlayer);
            }
        }
    }

    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (DeepStoragePlus.gettingInput.containsKey(player.getUniqueId()) && DeepStoragePlus.gettingInput.get(player.getUniqueId())) {
            if (event.getMessage().equalsIgnoreCase("cancel")) {
                DeepStoragePlus.gettingInput.put(player.getUniqueId(), false);
                DeepStoragePlus.openIOInv.put(player, true);
                event.setCancelled(true);
                return;
            }
            Inventory openIO = main.stashedIO.get(player.getUniqueId());
            ItemStack lock = createDSULock(openIO);
            ItemMeta meta = lock.getItemMeta();
            List<String> lore = meta.getLore();
            if (getLockedUsers(lock).size() == 0) {
                lore.set(lore.size() - 1, ChatColor.RED + LanguageManager.getValue("locked"));
            }
            lore.add(ChatColor.WHITE + event.getMessage());
            meta.setLore(lore);
            lock.setItemMeta(meta);
            openIO.setItem(53, lock);

            DeepStoragePlus.gettingInput.put(player.getUniqueId(), false);
            DeepStoragePlus.openIOInv.put(player, true);

            event.setCancelled(true);
        }
    }

    public void addText() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
            @Override
            public void run() {
                for (Player player : DeepStoragePlus.openIOInv.keySet()) {
                    if (DeepStoragePlus.stashedIO.containsKey(player.getUniqueId())) {
                        player.openInventory(DeepStoragePlus.stashedIO.get(player.getUniqueId()));
                        DeepStoragePlus.openIOInv.remove(player);
                        DeepStoragePlus.stashedIO.remove(player.getUniqueId());
                    }
                }
            }
        }, 1L, 5L);
    }
}
