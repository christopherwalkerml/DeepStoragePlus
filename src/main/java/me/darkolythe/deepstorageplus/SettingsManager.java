package me.darkolythe.deepstorageplus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

import static me.darkolythe.deepstorageplus.StorageUtils.stringToMat;

class SettingsManager {

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
    private static ItemStack getEmptyInputSlot() {
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
    private static ItemStack getEmptyOutputSlot() {
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
