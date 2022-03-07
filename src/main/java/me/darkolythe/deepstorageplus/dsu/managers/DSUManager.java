package me.darkolythe.deepstorageplus.dsu.managers;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.darkolythe.deepstorageplus.DeepStoragePlus;
import me.darkolythe.deepstorageplus.utils.ItemList;
import me.darkolythe.deepstorageplus.utils.LanguageManager;
import me.darkolythe.deepstorageplus.utils.item.misc.DSUWall;
import me.darkolythe.deepstorageplus.utils.item.misc.EmptyStorageSlot;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

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
        Material mat = item.getType();
        boolean isvaliditem = addToDSU(item, player.getOpenInventory().getTopInventory(), player); //try to add item to dsu
        main.dsuupdatemanager.updateItems(player.getOpenInventory().getTopInventory(), mat);
        if (item.getAmount() > 0 && isvaliditem) {
            player.sendMessage(DeepStoragePlus.prefix + ChatColor.RED + LanguageManager.getValue("containersfull"));
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
        if (IOItem == null
                || !IOItem.hasItemMeta()
                || !IOItem.getItemMeta().hasDisplayName()
                || !IOItem.getItemMeta().getDisplayName().equals(ChatColor.WHITE + LanguageManager.getValue("dsuioconfig"))) {
            inv.setItem(53, createIOItem(player));
        }
    }

    public static ItemStack createIOItem(Player player) {
        ItemStack settings = new ItemStack(Material.REDSTONE);
        ItemMeta settingsmeta = settings.getItemMeta();
        settingsmeta.setDisplayName(ChatColor.WHITE + ChatColor.stripColor(LanguageManager.getValue("dsuioconfig")));
        settingsmeta.setLore(Arrays.asList(ChatColor.GRAY + LanguageManager.getValue("input") + ": " + ChatColor.BLUE + LanguageManager.getValue("all"),
                ChatColor.GRAY + LanguageManager.getValue("output") + ": " + ChatColor.BLUE + LanguageManager.getValue("none"),
                ChatColor.GRAY + LanguageManager.getValue("sortingby") + ": " + ChatColor.BLUE + LanguageManager.getValue("container"),
                ChatColor.GRAY + LanguageManager.getValue("owner") + ": " + ChatColor.BLUE + player.getName(),
                ChatColor.RED + LanguageManager.getValue("locked")));
        settingsmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        settingsmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        settingsmeta.setUnbreakable(true);
        settings.setItemMeta(settingsmeta);

        NBTItem nbt = new NBTItem(settings);
        nbt.setString("dsu_owner_uuid", player.getUniqueId().toString());
        settings = nbt.getItem();

        settings.setDurability((short) 130);
        return settings;
    }

    
    private static ItemStack dsuWall;
    /*
    Create a dsu Wall item to fill the dsu Inventory
     */
    public static ItemStack getDSUWall() {
    	if (dsuWall != null)
    		return dsuWall;
    	
    	return dsuWall = new DSUWall()
            .setName(ChatColor.DARK_GRAY + LanguageManager.getValue("dsuwalls"))
            .setItemMeta()
            .getItem();
    }

    /*
    Create an Empty Block item to fill the dsu Inventory
     */
    public static ItemStack getEmptyBlock() {
        return new EmptyStorageSlot()
            .setName(ChatColor.YELLOW + LanguageManager.getValue("emptystorageblock"))
            .setItemMeta()
            .getItem();
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
        boolean foundColon = false;
        for (int z = 0; z < lore.length(); z++) {
            if (spaceAt != -1) {
                data += lore.charAt(z);
            }
            if (lore.charAt(z) == ' ' && foundColon) {
                spaceAt = z;
            }
            if (lore.charAt(z) == ':') {
                foundColon = true;
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
    public static HashSet<Material> getTypes(List<String> lore) {
        LinkedHashSet<Material> list = new LinkedHashSet<>();
        if (lore == null) {
            return list;
        }
        for (String str : lore) {
            if (str.contains(" - ") && !str.contains(LanguageManager.getValue("empty"))) {
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
        String[] strl = lore.replace(ChatColor.WHITE + " - ", "").split("\\s+");
        for (String s : strl) {
            if (!s.equals(strl[strl.length - 1])) {
                mat += s + " ";
            }
        }
        return Material.getMaterial(mat.trim().replaceAll(" ", "_").toUpperCase());
    }

    /*
    Update the container with the itemstack being added
     */
    public static void addDataToContainer(ItemStack container, ItemStack item) {
        if (container.getItemMeta() != null && ItemList.isStorageContainerItem(container)) {
            Material mat = item.getType();
            int amount = item.getAmount();

            int storage = countStorage(container, LanguageManager.getValue("currentstorage") + ": ");
            int types = countStorage(container, LanguageManager.getValue("currenttypes") + ": ");
            
            HashSet<Material> mats = getTypes(container.getItemMeta().getLore());
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
    This method loops until the item trying to be added is either done being added, or the containers run out of memory.
     */
    public static boolean addToDSUSilent(ItemStack toAdd, Inventory inv) {
        if (hasNoMeta(toAdd)) { //items being stored cannot have any special features. ie: damage, enchants, name, lore.
            for (int i = 0; i < 5; i++) {
                ItemStack container = inv.getItem(8 + (9 * i));
                if (container == null) {
                    continue;
                }
                addDataToContainer(container, toAdd); //add the item to the current loop container
                if (toAdd.getAmount() < 1) { //if the item amount is greater than 0, it means there are still items to put in the containers
                    break;
                }
            }
        } else {
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
            String cleanLore = lore.get(i).replace(ChatColor.WHITE.toString(), "").replace(" - ", "");
            if (!lore.get(i).contains(" - " + LanguageManager.getValue("empty")) && cleanLore.equals(matToString(mat) + " " + getMaterialAmount(lore.get(i)))) {
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
        int len = str.split("\\s+").length;
        String matAmt = str.split("\\s+")[len - 1];
        return Integer.parseInt(matAmt);
    }

    public static int getTotalMaterialAmount(Inventory inv, Material mat) {
        int amount = 0;
        for (int i = 0; i < 5; i++) {
            ItemStack container = inv.getItem(8 + (9 * i));
            if (container != null && container.getItemMeta() != null && container.getItemMeta().hasLore()) {
                for (String lore : container.getItemMeta().getLore()) {
                    if (!lore.contains(" - " + LanguageManager.getValue("empty")) && lore.contains("-")) {
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
    Get a complete list of types in a DSU
     */
    public static Set<Material> getTotalTypes(Inventory dsu) {
        LinkedHashSet<Material> list = new LinkedHashSet<>();
        for (int i = 0; i < 5; i++) {
            ItemStack container = dsu.getItem(8 + (9 * i));
            if (container != null && container.getItemMeta() != null && container.getItemMeta().getDisplayName().contains(LanguageManager.getValue("storagecontainer"))) {
                HashSet<Material> mats = DSUManager.getTypes(container.getItemMeta().getLore());
                list.addAll(mats);
            }
        }
        return list;
    }

    public static boolean dsuContainsType(Inventory dsu, Material material) {
        for (int i = 0; i < 5; i++) {
            ItemStack container = dsu.getItem(8 + (9 * i));
            if (container != null && container.getItemMeta() != null && container.getItemMeta().getDisplayName().contains(LanguageManager.getValue("storagecontainer"))) {
                HashSet<Material> mats = DSUManager.getTypes(container.getItemMeta().getLore());
                if (mats.contains(material)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
    Calculate how many items the player can take of a specific Material (max of the stack size of the Material). Remove that count from containers, and give it to the player.
     */
    public static int takeItems(Material mat, Inventory inv, int amt) {
        int amount = 0;
        int amountTaken = 0;
        for (int i = 4; i >= 0; i--) {
            if (mat != null) {
                if (amount != amt) {
                    ItemStack container = inv.getItem(8 + (9 * i));
                    if (container.hasItemMeta() && container.getItemMeta().hasDisplayName() && container.getItemMeta().getDisplayName().contains(LanguageManager.getValue("storagecontainer"))) {
                        for (int x = 2; x < DeepStoragePlus.maxTypes + 2; x++) {
                            if (getType(container.getItemMeta().getLore().get(x)) == mat) {
                                amount = Math.min(amount + getMaterialAmount(container.getItemMeta().getLore().get(x)), amt);
                                editContainerTypeAmount(container, mat, -Math.min(getMaterialAmount(container.getItemMeta().getLore().get(x)), amt - amountTaken));
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
}
