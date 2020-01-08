package me.darkolythe.deepstorageplus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.darkolythe.deepstorageplus.StorageUtils.*;

public class DSUManager {

    /*
    Add an item to the DSU
     */
    static void addItemToDSU(ItemStack item, Player player) {
        addToDSU(item, player.getOpenInventory().getTopInventory(), player); //try to add item to DSU
        updateItems(player.getOpenInventory().getTopInventory(), player.getOpenInventory().getTopInventory().getViewers());
        if (item.getAmount() > 0) {
            player.sendMessage(DeepStoragePlus.prefix + ChatColor.RED.toString() + "Storage containers are full");
        }
    }

    /*
    Create the DSU inventory and make it so that it's correct upon opening
    */
    static void verifyInventory(Inventory inv) {
        for (int i = 0; i < 6; i++) {
            inv.setItem(7 + (9 * i), getDSUWall());
        }

        for (int i = 0; i < 5; i++) {
            if (inv.getItem(8 + (9 * i)) == null) {
                inv.setItem(8 + (9 * i), getEmptyBlock());
            }
        }

        ItemStack IOItem = inv.getItem(53);
        if (IOItem == null || !IOItem.hasItemMeta() || !IOItem.getItemMeta().hasDisplayName() || !IOItem.getItemMeta().getDisplayName().equals("DSU IO Configuration")) {
            ItemStack settings = new ItemStack(Material.STONE_SHOVEL);
            ItemMeta settingsmeta = settings.getItemMeta();
            settingsmeta.setDisplayName(ChatColor.WHITE + "DSU IO Configuration");
            settingsmeta.setLore(Arrays.asList(ChatColor.GRAY + "Input: " + ChatColor.BLUE + "all",
                    ChatColor.GRAY + "Output: " + ChatColor.BLUE + "none",
                    ChatColor.GRAY + "Sorting By: " + ChatColor.BLUE + "container"));
            settingsmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            settingsmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            settingsmeta.setUnbreakable(true);
            settings.setItemMeta(settingsmeta);
            settings.setDurability((short) 130);
            inv.setItem(53, settings);
        }
    }

    /*
    Create a DSU Wall item to fill the DSU Inventory
     */
    static ItemStack getDSUWall() {
        ItemStack border = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta bordermeta = border.getItemMeta();
        bordermeta.setDisplayName(ChatColor.DARK_GRAY + "DSU Walls");
        border.setItemMeta(bordermeta);

        return border;
    }

    /*
    Create an Empty Block item to fill the DSU Inventory
     */
    static ItemStack getEmptyBlock() {
        ItemStack storage = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta storagemeta = storage.getItemMeta();
        storagemeta.setDisplayName(ChatColor.YELLOW + "Empty Storage Block");
        storage.setItemMeta(storagemeta);

        return storage;
    }

    /*
    Get the remaining data in a container by passing it a type field. Data or Type. (not literal strings)
     */
    static int countStorage(ItemStack container, String typeString) {
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
    static String getData(String lore) {
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
    static int getCurrentData(String data) {
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
    static int getMaxData(String data) {
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
    static List<Material> getTypes(List<String> lore) {
        List<Material> list = new ArrayList<>();

        for (String str : lore) {
            if (str.contains("-")) {
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
    static Material getType(String lore) {
        String mat = "";
        for (int i = 0; i < lore.length(); i++) {
            if (i >= 5) { //index 5 or more is the material part of the type lore
                mat += lore.charAt(i);
            }
        }
        return Material.getMaterial(mat.replaceAll("[0-9]", "").strip().replaceAll(" ", "_").toUpperCase());
    }

    /*
    Update the container with the itemstack being added
     */
    static void addDataToContainer(ItemStack container, ItemStack item) {
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
    static void addToDSU(ItemStack toAdd, Inventory inv, Player player) {
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
    static void editContainerStorage(ItemStack container, int amt, String type) {
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
        if (curAmt < (maxAmt / 2) + 1) {
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
    static void editContainerTypes(ItemStack container, ItemStack item) {
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
    static void editContainerTypeAmount(ItemStack container, Material mat, int amt) {
        ItemMeta meta = container.getItemMeta();
        List<String> lore = meta.getLore();
        for (int i = 2; i < DeepStoragePlus.maxTypes + 2; i++) {
            if (lore.get(i).contains(matToString(mat))) {
                String loreStr = lore.get(i);
                int curStorage = countStorage(container, "Current Storage: ");
                int newAmt = getMaterialAmount(loreStr) + Math.min(amt, curStorage);
                if (newAmt > 0) {
                    lore.set(i, ChatColor.WHITE.toString() + " - " + matToString(mat) + " " + newAmt);
                } else {
                    lore.set(i, ChatColor.GRAY + " - " + "empty"); //if an item is completely removed from the container, set the type to empty
                }
                meta.setLore(lore);
                container.setItemMeta(meta);
                if (newAmt <= 0) {
                    editContainerStorage(container, -1, "Current Types: "); //and then remove the type count from the counter. Must be done after meta is saved.
                }
                return;
            }
        }
    }

    /*
    Get the total amount of a material being stored in the DSU by checking all Containers
     */
    static int getMaterialAmount(String str) {
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

    /*
    Update the items in the DSU. This is done when items are added, taken, Storage Containers are added, taken, and when opening the DSU.
     */
    static void updateItems(Inventory inv, List<HumanEntity> players) {
        for (int i = 0; i < 5; i++) {
            ItemStack container = inv.getItem(8 + (9 * i));
            if (container.hasItemMeta() && container.getItemMeta().hasDisplayName() && container.getItemMeta().getDisplayName().contains("Storage Container")) {
                List<Material> mats = getTypes(container.getItemMeta().getLore());
                for (Material m : mats) {
                    ItemStack item = new ItemStack(m);
                    boolean canAdd = true;
                    for (ItemStack it : inv.getContents()) { //This section adds the items based on the Storage Containers
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
        for (int x = 0; x < 53; x++) {
            boolean isHere = false;
            if (x % 9 != 8 && x % 9 != 7) {
                if (inv.getItem(x) != null && inv.getItem(x).getType() != Material.AIR) {
                    for (int i = 0; i < 5; i++) {
                        ItemStack container = inv.getItem(8 + (9 * i));
                        if (container.hasItemMeta() && container.getItemMeta().hasDisplayName() && container.getItemMeta().getDisplayName().contains("Storage Container")) {
                            List<Material> mats = getTypes(container.getItemMeta().getLore());
                            for (Material m : mats) {
                                ItemStack it = new ItemStack(m);
                                if (it.equals(inv.getItem(x))) { //This section removes items that should no longer be there (whether it be they were removed, or their container was)
                                    isHere = true;
                                    break;
                                }
                            }
                        }
                        if (isHere) {
                            break;
                        }
                    }
                    if (!isHere) {
                        inv.getItem(x).setAmount(0);
                    }
                }
            }
        }
        inv.getLocation().getBlock().getState().update();
    }

    /*
    Calculate how many items the player can take of a specific Material (max of the stack size of the Material). Remove that count from containers, and give it to the player.
     */
    static int takeItems(Material mat, Inventory inv) {
        int amount = 0;
        int amountTaken = 0;
        for (int i = 4; i >= 0; i--) {
            if (amount != mat.getMaxStackSize()) {
                ItemStack container = inv.getItem(8 + (9 * i));
                if (container.hasItemMeta() && container.getItemMeta().hasDisplayName() && container.getItemMeta().getDisplayName().contains("Storage Container")) {
                    for (int x = 2; x < DeepStoragePlus.maxTypes + 2; x++) {
                        if (getType(container.getItemMeta().getLore().get(x)) == mat) {
                            amount = Math.min(amount + getMaterialAmount(container.getItemMeta().getLore().get(x)), mat.getMaxStackSize());
                            editContainerTypeAmount(container, mat, -Math.min(getMaterialAmount(container.getItemMeta().getLore().get(x)), mat.getMaxStackSize() - amountTaken));
                            editContainerStorage(container, amountTaken - amount, "Current Storage: ");
                            amountTaken += amount - amountTaken;
                            break;
                        }
                    }
                }
            }
        }
        return amount;
    }

    /*
    Take one item out of the most convenient storage container
     */
    static void takeOneItem(Material mat, Inventory inv) {
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

    /*
    Takes the DSU's inventory and lets the player choose an item to export from the list. (Also allow for other things? we shall see. maybe amount of item? Upgrades?)
     */
    static Inventory createIOInventory(Inventory DSUInv) {
        Inventory IOInv = Bukkit.createInventory(null, 54, ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + "DSU IO Configuration");

        for (int x = 0; x < 53; x++) {
            if (x % 9 != 8) {
                if (DSUInv.getItem(x) != null) {
                    IOInv.setItem(x, DSUInv.getItem(x).clone());
                }
            }
        }

        ItemStack IOItem = DSUInv.getItem(53);
        ItemMeta IOMeta = IOItem.getItemMeta();
        List<String> lore = IOMeta.getLore();

        if (lore.get(0).contains(ChatColor.BLUE + "all")) {
            IOInv.setItem(8, getEmptyInputSlot());
        } else {
            ItemStack newInput = getEmptyInputSlot();
            newInput.setType(stringToMat(lore.get(0), ChatColor.GRAY + "Input: " + ChatColor.GREEN));
            ItemMeta inputMeta = newInput.getItemMeta();
            inputMeta.setDisplayName(ChatColor.GRAY + "Input: " + lore.get(0).replace(ChatColor.GRAY + "Input: ", ""));
            inputMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click on this slot to clear selection."));
            newInput.setItemMeta(inputMeta);
            IOInv.setItem(8, newInput);
        }
        if (lore.get(1).contains(ChatColor.BLUE + "none")) {
            IOInv.setItem(17, getEmptyOutputSlot());
        } else {
            ItemStack newOutput = getEmptyOutputSlot();
            newOutput.setType(stringToMat(lore.get(1), ChatColor.GRAY + "Output: " + ChatColor.GREEN));
            ItemMeta outputMeta = newOutput.getItemMeta();
            outputMeta.setDisplayName(ChatColor.GRAY + "Output: " + lore.get(1).replace(ChatColor.GRAY + "Output: ", ""));
            outputMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click on this slot to clear selection."));
            newOutput.setItemMeta(outputMeta);
            IOInv.setItem(17, newOutput);
        }

        ItemStack sortSlot = new ItemStack(Material.COMPASS);
        ItemMeta sortMeta = sortSlot.getItemMeta();
        sortMeta.setDisplayName(ChatColor.GRAY + "Sorting By: " + lore.get(2).replace(ChatColor.GRAY + "Sorting By: ", ""));
        sortMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click to change sorting type.",
                ChatColor.BLUE + "container: " + ChatColor.GRAY + "sorts in order of items in storage containers.",
                ChatColor.BLUE + "alpha: " + ChatColor.GRAY + "sorts in alphabetical order.",
                ChatColor.BLUE + "amount: " + ChatColor.GRAY + "sorts by descending amount of items."));
        sortSlot.setItemMeta(sortMeta);
        IOInv.setItem(26, sortSlot);

        return IOInv;
    }

    /*
    Create an empty input slot item
     */
    static ItemStack getEmptyInputSlot() {
        ItemStack inputSlot = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta inputMeta = inputSlot.getItemMeta();
        inputMeta.setDisplayName(ChatColor.GRAY + "Input: " + ChatColor.BLUE + "all");
        inputMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click on this slot to start selection.",
                ChatColor.GRAY + "Then click an item in the DSU to input via hopper",
                ChatColor.GRAY + "Leave as 'all' to allow all items."));
        inputSlot.setItemMeta(inputMeta);

        return inputSlot;
    }

    /*
    Create an empty output slot item
     */
    static ItemStack getEmptyOutputSlot() {
        ItemStack outputSlot = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta outputMeta = outputSlot.getItemMeta();
        outputMeta.setDisplayName(ChatColor.GRAY + "Output: " + ChatColor.BLUE + "none");
        outputMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click on this slot to start selection.",
                ChatColor.GRAY + "Then click an item in the DSU to output via hopper."));
        outputSlot.setItemMeta(outputMeta);

        return outputSlot;
    }

    /*
    Initialize the selection tool by cancelling all other current selections and enchanting the current slot
     */
    static void startSelection(int slot, Inventory inv) {
        for (int i = 0; i < inv.getContents().length; i++) {
            if (inv.getItem(i) != null) {
                if (inv.getItem(i).getEnchantments().size() > 0) {
                    ItemStack newItem = inv.getItem(i);
                    newItem.removeEnchantment(Enchantment.DURABILITY);
                    inv.setItem(i, newItem);
                }
            }
        }
        ItemStack item;
        if (slot == 8) {
            item = getEmptyInputSlot();
        } else {
            item = getEmptyOutputSlot();
        }
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        inv.setItem(slot, item);
    }
}
