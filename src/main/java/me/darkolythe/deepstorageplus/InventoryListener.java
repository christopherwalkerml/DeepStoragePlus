package me.darkolythe.deepstorageplus;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
                updateItems(event.getInventory());
                player.updateInventory();
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
                        if (event.getClickedInventory() != player.getInventory()) {
                            if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.DARK_GRAY + "DSU Walls")) { //DSU WALLS
                                event.setCancelled(true);
                            } else if (event.getSlot() % 9 == 8 && event.getSlot() != 53) { //RIGHT BAR FOR STORAGE CELLS
                                if (event.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS_PANE) {
                                    event.setCancelled(true);
                                    if (event.getCursor() != null && event.getCursor().hasItemMeta()) { //if putting a Storage Container in the DSU
                                        if (event.getCursor().getItemMeta().getDisplayName().contains("Storage Container") && event.getCursor().getItemMeta().isUnbreakable()) {
                                            event.getInventory().setItem(event.getSlot(), event.getCursor());
                                            event.getCursor().setAmount(0);
                                        }
                                    }
                                } else if (event.getCursor() != null && event.getCursor().getType() != Material.AIR) {
                                    if (!(event.getCursor().hasItemMeta() && event.getCursor().getItemMeta().getDisplayName().contains("Storage Container") && event.getCursor().getItemMeta().isUnbreakable())) {
                                        event.setCancelled(true);
                                    } else if (event.isShiftClick()) {
                                        event.setCancelled(true);
                                    }
                                } else if (event.getCursor() == null || event.getCursor().getType() == Material.AIR) { //if taking a Storage Container out of the DSU
                                    event.setCancelled(true);
                                    player.setItemOnCursor(event.getCurrentItem().clone());
                                    event.getInventory().setItem(event.getSlot(), getEmptyBlock());
                                }
                            } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GRAY + "DSU IO Configuration")) { //BOTTOM RIGHT FOR SETTINGS
                                event.setCancelled(true);
                            }
                        }
                    } else if (event.getClickedInventory() == player.getInventory()) { //NOTE: key number press
                        if (event.isShiftClick()) {
                            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
                                addToDSU(event.getCurrentItem(), player.getOpenInventory().getTopInventory(), player); //try to add item to DSU
                                updateItems(player.getOpenInventory().getTopInventory());
                                player.updateInventory();
                                if (event.getCurrentItem().getAmount() > 0) {
                                    player.sendMessage(DeepStoragePlus.prefix + ChatColor.RED.toString() + "Storage containers are full");
                                }
                                event.setCancelled(true);
                            }
                        }
                    } else { //if click in DSU with item on cursor
                        if (event.getCursor() != null && event.getCursor().getType() != Material.AIR) {
                            addToDSU(event.getCursor(), event.getClickedInventory(), player); //try to add item to DSU
                            updateItems(event.getInventory());
                            player.updateInventory();
                            if (event.getCursor().getAmount() > 0) {
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

    /*
    Create the DSU inventory and make it so that it's correct upon opening
     */
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

    /*
    Create a DSU Wall item to fill the DSU Inventory
     */
    private static ItemStack getDSUWall() {
        ItemStack border = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta bordermeta = border.getItemMeta();
        bordermeta.setDisplayName(ChatColor.DARK_GRAY + "DSU Walls");
        border.setItemMeta(bordermeta);

        return border;
    }

    /*
    Create an Empty Block item to fill the DSU Inventory
     */
    private static ItemStack getEmptyBlock() {
        ItemStack storage = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta storagemeta = storage.getItemMeta();
        storagemeta.setDisplayName(ChatColor.YELLOW + "Empty Storage Block");
        storage.setItemMeta(storagemeta);

        return storage;
    }

    /*
    Get the remaining data in a container by passing it a type field. Data or Type. (not literal strings)
     */
    private static int countStorage(ItemStack container, String typeString) {
        int spaceTotal = 0;
        int spaceCur = 0;
        if (container != null && container.hasItemMeta() && container.getItemMeta().hasLore()) {
            ItemMeta meta = container.getItemMeta();
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

    /*
    Get the data portion of the lore string. Format: Type: data
     */
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

    /*
    Get the current data in the storage info format: Type: cur/max
     */
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

    /*
    Get the max data in the storage info format: Type: cur/max
     */
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

    /*
    Get the list of types that a container has
     */
    private static List<Material> getTypes(List<String> lore) {
        List<Material> list = new ArrayList<>();

        for (String str : lore) {
            if (str.contains("-")) {
                String mat = "";
                for (int i = 0; i < str.length(); i++) {
                    if (i >= 5) { //index 5 or more is the material part of the type lore
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

    /*
    Update the container with the itemstack being added
     */
    private static void addDataToContainer(ItemStack container, ItemStack item) {
        if (container.hasItemMeta() && container.getItemMeta().hasDisplayName() && container.getItemMeta().getDisplayName().contains("Storage Container")) {
            Material mat = item.getType();
            int amount = item.getAmount();

            int storage = countStorage(container, "Current Storage: ");
            int types = countStorage(container, "Current Types: ");
            List<Material> mats = getTypes(container.getItemMeta().getLore());

            int canAdd = Math.min(storage, amount);
            if (mats.contains(mat)) { //if the material is already stored in the container
                editContainerTypeAmount(container, mat, amount); //update material storage amount
                editContainerStorage(container, canAdd, "Current Storage: "); //update data storage amt
                item.setAmount(amount - canAdd); //update item amount. if this is not 0 in the main loop, it means this container ran out of memory - IMPORTANT TO NOTE <--------------------
            } else { //if the material isnt currently being stored in the container
                if (types > 0 && (getMaxData(getData(container.getItemMeta().getLore().get(0))) - getCurrentData(getData(container.getItemMeta().getLore().get(0))) > 0)) {
                    editContainerTypes(container, item); //add the material type to the container
                    editContainerStorage(container, canAdd, "Current Storage: "); //add amt to data storage
                    editContainerStorage(container, 1, "Current Types: "); //add 1 to types
                    item.setAmount(amount - canAdd);
                }
            }
        }
    }

    /*
    This method loops until the item trying to be added is either done being added, or the containers run out of memory.
     */
    private static void addToDSU(ItemStack toAdd, Inventory inv, Player player) {
        if (hasNoMeta(toAdd)) { //items being stored cannot have any special features. ie: damage, enchants, name, lore.
            for (int i = 0; i < 5; i++) {
                if (toAdd.getAmount() > 0) { //if the item amount is greater than 0, it means there are still items to put in the containers
                    addDataToContainer(inv.getItem(8 + (9 * i)), toAdd); //add the item to the current loop container
                } else {
                    break;
                }
            }
            player.updateInventory();
        } else {
            player.sendMessage(DeepStoragePlus.prefix + ChatColor.RED + "You can only store default items in the DSU");
        }
    }

    /*
        Update the storage info on the container with the type, amt being added, and container being edited.
     */
    private static void editContainerStorage(ItemStack container, int amt, String type) {
        ItemMeta meta = container.getItemMeta();
        List<String> lore = meta.getLore();
        String data;
        if (type.equals("Current Storage: ")) { //edit the line of lore depending on which storage field is being edited
            data = getData(meta.getLore().get(0));
        } else {
            data = getData(meta.getLore().get(1));
        }
        int curAmt = getCurrentData(data);
        int maxAmt = getMaxData(data);
        curAmt += amt;
        String colourStorage; //Edit the colour of the storage tag based on how much storage is available
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

    /*
    This method adds a new material type to the container and sets the ammount
     */
    private static void editContainerTypes(ItemStack container, ItemStack item) {
        Material mat = item.getType();
        ItemMeta meta = container.getItemMeta();
        List<String> lore = meta.getLore();
        for (int i = 2; i < DeepStoragePlus.maxTypes + 2; i++) {
            if (lore.get(i).contains("empty")) { //find the first empty material type and update it to the new material
                int curStorage = countStorage(container, "Current Storage: ");
                lore.set(i, ChatColor.WHITE.toString() + " - " + matToString(mat) + " " + Math.min(item.getAmount(), curStorage));
                meta.setLore(lore);
                container.setItemMeta(meta);
                return;
            }
        }
    }

    /*
    Edit the amount of a certain material being stored in a container.
     */
    private static void editContainerTypeAmount(ItemStack container, Material mat, int amt) {
        ItemMeta meta = container.getItemMeta();
        List<String> lore = meta.getLore();
        for (int i = 2; i < DeepStoragePlus.maxTypes + 2; i++) {
            if (lore.get(i).contains(matToString(mat))) {
                String loreStr = lore.get(i);
                String matAmt = "";
                for (int x = 0; x < loreStr.length(); x++) {
                    if (x >= 5) { //index 5 or more is the past the initial part of the type lore. Format: &x - Material #
                        if (Character.isDigit(loreStr.charAt(x))) {
                            matAmt += loreStr.charAt(x); //get the amount portion of the data
                        }
                    }
                }
                int curStorage = countStorage(container, "Current Storage: ");
                int newAmt = Integer.parseInt(matAmt) + Math.min(amt, curStorage);
                lore.set(i, ChatColor.WHITE.toString() + " - " + matToString(mat) + " " + newAmt);
                meta.setLore(lore);
                container.setItemMeta(meta);
                return;
            }
        }
    }

    private static String matToString(Material mat) {
        return WordUtils.capitalize(mat.toString().toLowerCase().replaceAll("_", " "));
    }

    private static void updateItems(Inventory inv) {
        for (int i = 0; i < 5; i++) {
            ItemStack container = inv.getItem(8 + (9 * i));
            if (container.hasItemMeta() && container.getItemMeta().hasDisplayName() && container.getItemMeta().getDisplayName().contains("Storage Container")) {
                List<Material> mats = getTypes(container.getItemMeta().getLore());
                for (Material m : mats) {
                    ItemStack item = new ItemStack(m);
                    boolean canAdd = true;
                    for (ItemStack it : inv.getContents()) {
                        if (item.equals(it)) {
                            canAdd = false;
                            break;
                        }
                    }
                    if (canAdd) {
                        inv.addItem(item);
                    }
                }
            }
        }
    }
}
