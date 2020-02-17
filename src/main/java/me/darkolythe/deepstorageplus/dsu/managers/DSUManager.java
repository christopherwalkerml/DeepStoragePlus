package me.darkolythe.deepstorageplus.dsu.managers;

import me.darkolythe.deepstorageplus.DeepStoragePlus;
import me.darkolythe.deepstorageplus.utils.LanguageManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.darkolythe.deepstorageplus.dsu.StorageUtils.*;

public class DSUManager {

    DeepStoragePlus main;
    public DSUManager(DeepStoragePlus plugin) {
        main = plugin;
    }

    /*
    Add an item to the dsu
     */
    public void addItemToDSU(ItemStack item, Player player) {
        boolean isvaliditem = addToDSU(item, player.getOpenInventory().getTopInventory(), player); //try to add item to dsu
        main.dsuupdatemanager.updateItems(player.getOpenInventory().getTopInventory());
        if (item.getAmount() > 0 && isvaliditem) {
            player.sendMessage(DeepStoragePlus.prefix + ChatColor.RED.toString() + LanguageManager.getValue("containersfull"));
        }
    }

    /*
    Create the dsu inventory and make it so that it's correct upon opening
    */
    public static void verifyInventory(Inventory inv, Player player) {
        for (int i = 0; i < 6; i++) {
            inv.setItem(7 + (9 * i), getDSUWall());
        }

        for (int i = 0; i < 5; i++) {
            if (inv.getItem(8 + (9 * i)) == null) {
                inv.setItem(8 + (9 * i), getEmptyBlock());
            }
        }

        ItemStack IOItem = inv.getItem(53);
        if (IOItem == null || !IOItem.hasItemMeta() || !IOItem.getItemMeta().hasDisplayName() || !IOItem.getItemMeta().getDisplayName().equals(LanguageManager.getValue("dsuioconfig"))) {
            inv.setItem(53, createIOItem(player));
        }
    }

    public static ItemStack createIOItem(Player player) {
        ItemStack settings = new ItemStack(Material.STONE_SHOVEL);
        ItemMeta settingsmeta = settings.getItemMeta();
        settingsmeta.setDisplayName(ChatColor.WHITE + LanguageManager.getValue("dsuioconfig"));
        settingsmeta.setLore(Arrays.asList(ChatColor.GRAY + LanguageManager.getValue("input") + ": " + ChatColor.BLUE + LanguageManager.getValue("all"),
                ChatColor.GRAY + LanguageManager.getValue("output") + ": " + ChatColor.BLUE + LanguageManager.getValue("none"),
                ChatColor.GRAY + LanguageManager.getValue("sortingby") + ": " + ChatColor.BLUE + LanguageManager.getValue("container"),
                ChatColor.RED + LanguageManager.getValue("locked"),
                ChatColor.WHITE + player.getName()));
        settingsmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        settingsmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        settingsmeta.setUnbreakable(true);
        settings.setItemMeta(settingsmeta);
        settings.setDurability((short) 130);
        return settings;
    }

    /*
    Create a dsu Wall item to fill the dsu Inventory
     */
    public static ItemStack getDSUWall() {
        ItemStack border = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta bordermeta = border.getItemMeta();
        bordermeta.setDisplayName(ChatColor.DARK_GRAY + LanguageManager.getValue("dsuwalls"));
        border.setItemMeta(bordermeta);

        return border;
    }

    /*
    Create an Empty Block item to fill the dsu Inventory
     */
    public static ItemStack getEmptyBlock() {
        ItemStack storage = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta storagemeta = storage.getItemMeta();
        storagemeta.setDisplayName(ChatColor.YELLOW + LanguageManager.getValue("emptystorageblock"));
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
    public static List<Material> getTypes(List<String> lore) {
        List<Material> list = new ArrayList<>();

        for (String str : lore) {
            if (str.contains("-") && !str.contains(LanguageManager.getValue("empty"))) {
                Material mat = getType(str);
                if (mat != null) {
                    list.add(mat);
                }
            }
        }
        return list;
    }

    /*
    Get the type of Material from a specific lore line of a Storage Container
     */
    private static Material getType(String lore) {
        String mat = "";
        for (int i = 0; i < lore.length(); i++) {
            if (i >= 5) { //index 5 or more is the material part of the type lore
                mat += lore.charAt(i);
            }
        }
        return Material.getMaterial(mat.replaceAll("[0-9]", "").trim().replaceAll(" ", "_").toUpperCase());
    }

    /*
    Update the container with the itemstack being added
     */
    public static void addDataToContainer(ItemStack container, ItemStack item) {
        if (container.hasItemMeta() && container.getItemMeta().hasDisplayName() && container.getItemMeta().getDisplayName().contains(LanguageManager.getValue("storagecontainer"))) {
            Material mat = item.getType();
            int amount = item.getAmount();

            int storage = countStorage(container, LanguageManager.getValue("currentstorage") + ": ");
            int types = countStorage(container, LanguageManager.getValue("currenttypes") + ": ");
            List<Material> mats = getTypes(container.getItemMeta().getLore());

            int canAdd = Math.min(storage, amount);
            if (mats.contains(mat)) { //if the material is already stored in the container
                editContainerTypeAmount(container, mat, amount); //update material storage amount
                editContainerStorage(container, canAdd, LanguageManager.getValue("currentstorage") + ": "); //update data storage amt
                item.setAmount(amount - canAdd); //update item amount. if this is not 0 in the main loop, it means this container ran out of memory - IMPORTANT TO NOTE <--------------------
            } else { //if the material isnt currently being stored in the container
                if (types > 0 && (getMaxData(getData(container.getItemMeta().getLore().get(0))) - getCurrentData(getData(container.getItemMeta().getLore().get(0))) > 0)) {
                    editContainerTypes(container, item); //add the material type to the container
                    editContainerStorage(container, canAdd, LanguageManager.getValue("currentstorage") + ": "); //add amt to data storage
                    editContainerStorage(container, 1, LanguageManager.getValue("currenttypes") + ": "); //add 1 to types
                    item.setAmount(amount - canAdd);
                }
            }
        }
    }

    /*
    This method loops until the item trying to be added is either done being added, or the containers run out of memory.
     */
    public static boolean addToDSU(ItemStack toAdd, Inventory inv, Player player) {
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
            player.sendMessage(DeepStoragePlus.prefix + ChatColor.RED + LanguageManager.getValue("onlydefaultitems"));
            return false;
        }
        return true;
    }

    /*
        Update the storage info on the container with the type, amt being added, and container being edited.
     */
    private static void editContainerStorage(ItemStack container, int amt, String type) {
        ItemMeta meta = container.getItemMeta();
        List<String> lore = meta.getLore();
        String data;
        if (type.equals(LanguageManager.getValue("currentstorage") + ": ")) { //edit the line of lore depending on which storage field is being edited
            data = getData(meta.getLore().get(0));
        } else {
            data = getData(meta.getLore().get(1));
        }
        int curAmt = getCurrentData(data);
        int maxAmt = getMaxData(data);
        curAmt += amt;
        String colourStorage; //Edit the colour of the storage tag based on how much storage is available
        if (curAmt < (maxAmt / 2) + 1) {
            colourStorage = ChatColor.GREEN.toString();
        } else if (curAmt < maxAmt) {
            colourStorage = ChatColor.YELLOW.toString();
        } else {
            colourStorage = ChatColor.RED.toString();
        }
        if (type.equals(LanguageManager.getValue("currentstorage") + ": ")) {
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
            if (lore.get(i).contains(LanguageManager.getValue("empty"))) { //find the first empty material type and update it to the new material
                int curStorage = countStorage(container, LanguageManager.getValue("currentstorage") + ": ");
                System.out.println("xx   " + matToString(mat));
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
            if (!lore.get(i).contains(" - empty") && lore.get(i).contains(matToString(mat) + " " + getMaterialAmount(lore.get(i)))) {
                String loreStr = lore.get(i);
                int curStorage = countStorage(container, LanguageManager.getValue("currentstorage") + ": ");
                int newAmt = getMaterialAmount(loreStr) + Math.min(amt, curStorage);
                if (newAmt > 0) {
                    lore.set(i, ChatColor.WHITE.toString() + " - " + matToString(mat) + " " + newAmt);
                } else {
                    lore.set(i, ChatColor.GRAY + " - " + LanguageManager.getValue("empty")); //if an item is completely removed from the container, set the type to empty
                }
                meta.setLore(lore);
                container.setItemMeta(meta);
                if (newAmt <= 0) {
                    editContainerStorage(container, -1, LanguageManager.getValue("currenttypes") + ": "); //and then remove the type count from the counter. Must be done after meta is saved.
                }
                return;
            }
        }
    }

    /*
    Get the amount of material being stored on the one line of lore in a container
     */
    private static int getMaterialAmount(String str) {
        String matAmt = "";
        for (int x = 0; x < str.length(); x++) {
            if (x >= 5) { //index 5 or more is the past the initial part of the type lore. Format: &x - Material #
                if (Character.isDigit(str.charAt(x))) {
                    matAmt += str.charAt(x); //get the amount portion of the data
                }
            }
        }
        return Integer.parseInt(matAmt);
    }

    static int getTotalMaterialAmount(Inventory inv, Material mat) {
        int amount = 0;
        for (int i = 0; i < 5; i++) {
            ItemStack container = inv.getItem(8 + (9 * i));
            if (container != null && container.getItemMeta() != null && container.getItemMeta().hasLore()) {
                for (String lore : container.getItemMeta().getLore()) {
                    if (!lore.contains(" - empty") && lore.contains("-")) {
                        if (lore.contains(" - " + matToString(mat) + " " + getMaterialAmount(lore))) {
                            amount += getMaterialAmount(lore);
                        }
                    }
                }
            }
        }
        return amount;
    }

    /*
    Calculate how many items the player can take of a specific Material (max of the stack size of the Material). Remove that count from containers, and give it to the player.
     */
    public static int takeItems(Material mat, Inventory inv) {
        int amount = 0;
        int amountTaken = 0;
        for (int i = 4; i >= 0; i--) {
            if (mat != null) {
                if (amount != mat.getMaxStackSize()) {
                    ItemStack container = inv.getItem(8 + (9 * i));
                    if (container.hasItemMeta() && container.getItemMeta().hasDisplayName() && container.getItemMeta().getDisplayName().contains(LanguageManager.getValue("storagecontainer"))) {
                        for (int x = 2; x < DeepStoragePlus.maxTypes + 2; x++) {
                            if (getType(container.getItemMeta().getLore().get(x)) == mat) {
                                amount = Math.min(amount + getMaterialAmount(container.getItemMeta().getLore().get(x)), mat.getMaxStackSize());
                                editContainerTypeAmount(container, mat, -Math.min(getMaterialAmount(container.getItemMeta().getLore().get(x)), mat.getMaxStackSize() - amountTaken));
                                editContainerStorage(container, amountTaken - amount, LanguageManager.getValue("currentstorage") + ": ");
                                amountTaken += amount - amountTaken;
                                break;
                            }
                        }
                    }
                } else {
                    break;
                }
            }
        }
        return amount;
    }

    /*
    Take one item out of the most convenient storage container
     */
    public static void takeOneItem(Material mat, Inventory inv) {
        int amount = 0;
        for (int i = 4; i >= 0; i--) {
            if (amount != 1) {
                ItemStack container = inv.getItem(8 + (9 * i));
                if (container.hasItemMeta() && container.getItemMeta().hasDisplayName() && container.getItemMeta().getDisplayName().contains("Storage Container")) {
                    for (int x = 2; x < DeepStoragePlus.maxTypes + 2; x++) {
                        if (getType(container.getItemMeta().getLore().get(x)) == mat) {
                            amount = Math.min(getMaterialAmount(container.getItemMeta().getLore().get(x)), 1);
                            editContainerTypeAmount(container, mat, -1);
                            editContainerStorage(container, -1, "Current Storage: ");
                            break;
                        }
                    }
                }
            }
        }
    }
}
