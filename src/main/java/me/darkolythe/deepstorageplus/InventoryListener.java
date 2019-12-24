package me.darkolythe.deepstorageplus;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class InventoryListener implements Listener {

    private DeepStoragePlus main;
    public InventoryListener(DeepStoragePlus plugin) {
        this.main = plugin; // set it equal to an instance of main
    }

    @EventHandler
    private void onStorageOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if (event.getView().getTitle().equals(DeepStoragePlus.DSUname)) {
                verifyInventory(event.getInventory());
            }
        }
    }

    @EventHandler
    private void onStorageInteract(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();
                if (event.getView().getTitle().equals(DeepStoragePlus.DSUname)) {
                    if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()) {
                        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.DARK_GRAY + "DSU Walls")) { //DSU WALLS
                            event.setCancelled(true);
                        } else if (event.getSlot() % 9 == 8 && event.getSlot() != 53) { //RIGHT BAR FOR STORAGE CELLS
                            if (event.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS_PANE) {
                                event.setCancelled(true);
                                if (event.getCursor() != null && event.getCursor().hasItemMeta()) {
                                    if (event.getCursor().getItemMeta().getDisplayName().contains("Storage Container") && event.getCursor().getItemMeta().isUnbreakable()) {
                                        event.getInventory().setItem(event.getSlot(), event.getCursor());
                                        event.getCursor().setAmount(0);
                                    }
                                }
                            } else if (event.getCursor() != null && event.getCursor().getType() != Material.AIR) {
                                if (!(event.getCursor().hasItemMeta() && event.getCursor().getItemMeta().getDisplayName().contains("Storage Container") && event.getCursor().getItemMeta().isUnbreakable())) {
                                    event.setCancelled(true);
                                }
                            } else if (event.getCursor() == null || event.getCursor().getType() == Material.AIR) {
                                event.setCancelled(true);
                                player.setItemOnCursor(event.getCurrentItem().clone());
                                event.getInventory().setItem(event.getSlot(), getEmptyBlock());
                            }
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GRAY + "DSU IO Configuration")) { //BOTTOM RIGHT FOR SETTINGS
                            event.setCancelled(true);
                        }
                    } else if (event.getClickedInventory() == player.getInventory()) { //NOTE: key number press, double shift, shift
                        if (event.isShiftClick()) {
                            if (event.getClick() != ClickType.DOUBLE_CLICK) {

                            } else {

                            }
                        }
                    } else { //if click in DSU with item on cursor
                        if (event.getCursor() != null && event.getCursor().getType() != Material.AIR) {
                            addToDSU(event.getCursor(), event.getClickedInventory(), player);
                            if (event.getCursor() != null && (event.getCursor().getType() != Material.AIR || event.getCursor().getAmount() > 0)) {
                                player.sendMessage(DeepStoragePlus.prefix + ChatColor.RED.toString() + "Storage containers are full");
                            }
                            event.setCancelled(true);
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

    private static void verifyInventory(Inventory inv) {
        ItemStack border = getDSUWall();
        for (int i = 0; i < 6; i++) {
            inv.setItem(7 + (9 * i), border.clone());
        }

        ItemStack storage = getEmptyBlock();
        for (int i = 0; i < 5; i++) {
            if (inv.getItem(8 + (9 * i)) == null) {
                inv.setItem(8 + (9 * i), storage.clone());
            }
        }

        ItemStack settings = new ItemStack(Material.STONE_SHOVEL);
        ItemMeta settingsmeta = settings.getItemMeta();
        settingsmeta.setDisplayName(ChatColor.GRAY + "DSU IO Configuration");
        settingsmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        settingsmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        settingsmeta.setUnbreakable(true);
        settings.setItemMeta(settingsmeta);
        settings.setDurability((short) 130);
        inv.setItem(53, settings);
    }

    private static ItemStack getDSUWall() {
        ItemStack border = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta bordermeta = border.getItemMeta();
        bordermeta.setDisplayName(ChatColor.DARK_GRAY + "DSU Walls");
        border.setItemMeta(bordermeta);

        return border;
    }

    private static ItemStack getEmptyBlock() {
        ItemStack storage = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta storagemeta = storage.getItemMeta();
        storagemeta.setDisplayName(ChatColor.YELLOW + "Empty Storage Block");
        storage.setItemMeta(storagemeta);

        return storage;
    }

    private static int countStorage(ItemStack item, String typeString) {
        int spaceTotal = 0;
        int spaceCur = 0;
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore()) {
            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.getLore();
            for (String l : lore) {
                if (l.contains(typeString)) {
                    String data = getData(l);
                    spaceCur += getCurrentData(data);
                    spaceTotal += getMaxData(data);
                }
            }
        }
        return spaceTotal - spaceCur;
    }

    private static String getData(String lore) {
        int spaceAt = -1;
        String data = "";
        for (int z = 0; z < lore.length(); z++) {
            if (spaceAt != -1) {
                data += lore.charAt(z);
            }
            if (lore.charAt(z) == ' ' && z > 15) {
                spaceAt = z;
            }
        }
        return data;
    }

    private static int getCurrentData(String data) {
        String curData = "";
        int slashAt = -1;
        for (int z = 0; z < data.length(); z++) {
            if (data.charAt(z) == '/') {
                slashAt = z;
            }
            if (slashAt == -1) {
                curData += data.charAt(z);
            }
        }
        return Integer.parseInt(curData);
    }

    private static int getMaxData(String data) {
        String maxData = "";
        int slashAt = -1;
        for (int z = 0; z < data.length(); z++) {
            if (slashAt != -1) {
                maxData += data.charAt(z);
            }
            if (data.charAt(z) == '/') {
                slashAt = z;
            }
        }
        return Integer.parseInt(maxData);
    }

    private static List<Material> getTypes(List<String> lore) {
        List<Material> list = new ArrayList<>();

        for (String str : lore) {
            if (str.contains("-")) {
                String mat = "";
                for (int i = 0; i < str.length(); i++) {
                    if (i >= 5) { //index 5 or more is the data part of the type lore
                        mat += str.charAt(i);
                    }
                }
                if (!mat.equals("empty")) {
                    list.add(Material.getMaterial(mat.replaceAll("[0-9]", "").strip().replaceAll(" ", "_").toUpperCase()));
                }
            }
        }

        return list;
    }

    private static boolean hasNoMeta(ItemStack item) {
        if (item.hasItemMeta()) {
            return false;
        } else if (item.getDurability() != 0) {
            return false;
        } else if (item.getEnchantments().size() > 0) {
            return false;
        }
        return true;
    }

    private static void addDataToContainer(ItemStack container, ItemStack item) {
        if (container.hasItemMeta() && container.getItemMeta().hasDisplayName() && container.getItemMeta().getDisplayName().contains("Storage Container")) {
            Material mat = item.getType();
            int amount = item.getAmount();

            int storage = countStorage(container, "Current Storage: ");
            int types = countStorage(container, "Current Types: ");
            List<Material> mats = getTypes(container.getItemMeta().getLore());

            if (mats.contains(mat)) {
                int canAdd = Math.min(storage, amount);
                editContainerStorage(container, canAdd, "Current Storage: ");
                item.setAmount(amount - canAdd);
            } else {
                if (types > 0) {
                    int canAdd = Math.min(storage, amount);
                    editContainerStorage(container, canAdd, "Current Storage: ");
                    ItemStack cloneItem = item.clone();
                    item.setAmount(amount - canAdd);
                    editContainerStorage(container, 1, "Current Types: ");
                    editContainerTypes(container, cloneItem);
                }
            }
        }
    }

    private static void addToDSU(ItemStack toAdd, Inventory inv, Player player) {
        System.out.println(toAdd);
        if (hasNoMeta(toAdd)) {
            for (int i = 0; i < 5; i++) {
                if (toAdd.getAmount() > 0) {
                    addDataToContainer(inv.getItem(8 + (9 * i)), toAdd);
                } else {
                    break;
                }
            }
            player.updateInventory();
        } else {
            player.sendMessage(DeepStoragePlus.prefix + ChatColor.RED + "You can only store default items in the DSU");
        }
    }

    private static void editContainerStorage(ItemStack container, int amt, String type) {
        ItemMeta meta = container.getItemMeta();
        List<String> lore = meta.getLore();
        String data;
        if (type.equals("Current Storage: ")) {
            data = getData(meta.getLore().get(0));
        } else {
            data = getData(meta.getLore().get(1));
        }
        int curAmt = getCurrentData(data);
        int maxAmt = getMaxData(data);
        curAmt += amt;
        String colourStorage; //Edit the colour of the storage tag based on how much is available
        if (curAmt < maxAmt / 2) {
            colourStorage = ChatColor.GREEN.toString();
        } else if (curAmt < maxAmt) {
            colourStorage = ChatColor.YELLOW.toString();
        } else {
            colourStorage = ChatColor.RED.toString();
        }
        if (type.equals("Current Storage: ")) {
            lore.set(0, colourStorage + type + curAmt + "/" + maxAmt);
        } else {
            lore.set(1, colourStorage + type + curAmt + "/" + maxAmt);
        }
        meta.setLore(lore);
        container.setItemMeta(meta);
    }

    private static void editContainerTypes(ItemStack container, ItemStack item) {
        Material mat = item.getType();
        ItemMeta meta = container.getItemMeta();
        List<String> lore = meta.getLore();
        for (int i = 2; i < DeepStoragePlus.maxTypes + 2; i++) {
            if (lore.get(i).contains("empty")) {
                lore.set(i, ChatColor.WHITE.toString() + " - " + WordUtils.capitalize(mat.toString().toLowerCase().replaceAll("_", " ") + " " + item.getAmount()));
                meta.setLore(lore);
                container.setItemMeta(meta);
                return;
            }
        }
    }

    private static void editContainerTypeAmount(ItemStack container, Material mat, int amt) { //TODO add item amount calculation. Then go back to inventory click types
        ItemMeta meta = container.getItemMeta();
        List<String> lore = meta.getLore();
        for (int i = 2; i < DeepStoragePlus.maxTypes + 2; i++) {
            if (lore.get(i).contains(WordUtils.capitalize(mat.toString().toLowerCase().replaceAll("_", " ")))) {

            }
        }
    }
}
