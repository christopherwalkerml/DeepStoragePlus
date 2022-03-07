package me.darkolythe.deepstorageplus.dsu;

import me.darkolythe.deepstorageplus.dsu.managers.DSUManager;
import me.darkolythe.deepstorageplus.dsu.managers.SorterManager;
import me.darkolythe.deepstorageplus.utils.ItemList;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class StorageUtils {

    /*
    Check if the item "has no meta" which counts enchants, damage, lore, name, etc.
     */
    public static boolean hasNoMeta(ItemStack item) {
        if (item.getDurability() != 0) {
            return false;
        }
        if (item.getType().toString().contains("SHULKER_BOX")) {
            return false;
        }
        if (item.hasItemMeta()) {
            if (item.getItemMeta().hasEnchants()) {
                return false;
            }
            if (item.getItemMeta().hasDisplayName()) {
                return false;
            }
            if (item.getType() == Material.ENCHANTED_BOOK) {
                return false;
            }
            if (item.getType() == Material.FIREWORK_ROCKET) {
                return false;
            }
            if (item.getType().toString().contains("POTION")) {
                return false;
            }
            if (item.getType() == Material.TIPPED_ARROW) {
                return false;
            }
            if (item.getItemMeta().hasLore()) {
                if (item.getItemMeta().getLore().get(0).contains("Item Count: ")) {
                    if (item.getDurability() != 0) {
                        return false;
                    }
                    if (item.getEnchantments().size() > 0) {
                        return false;
                    }
                    return true;
                }
                return false;
            }
        }
        return true;
    }

    /*
    Turns a Material into a String. ex: EMERALD_ORE -> Emerald Ore
     */
    public static String matToString(Material mat) {
        return WordUtils.capitalize(mat.toString().toLowerCase().replaceAll("_", " "));
    }

    /*
    Turns a String into a Material. ex: Emerald Ore -> EMERALD_ORE
     */
    public static Material stringToMat(String str, String remStr) {
        return Material.valueOf(str.replace(remStr, "").toUpperCase().replace(" ", "_").toUpperCase());
    }

    public static boolean isDSU(Inventory inv) {
        if (inv.getSize() != 54)
            return false;

        if (inv.getType() != InventoryType.CHEST)
            return false;

        for (ItemStack i : inv.getContents()) {
            if (i != null) {
                // Check if we are in the DSU IO Configuration
                if (i.getType().equals(Material.TRIPWIRE_HOOK)
                        && i.getItemMeta() != null
                        && i.getItemMeta().hasDisplayName()
                        && i.getItemMeta().getDisplayName().equals(ChatColor.BLUE + "Lock DSU")) {
                    return false;
                }
            }
        }

        int[] slots = {7, 16, 25, 34, 43, 52};
        boolean isDSU = false;

        for (int i : slots) {
            ItemStack temp = inv.getItem(i);
            
            if (temp != null && temp.equals(DSUManager.getDSUWall()))
                isDSU = true;
        }
    
        return isDSU;
    }

    public static boolean isSorter(Inventory inv) {
        if (inv.getSize() != 54)
            return false;

        if (inv.getType() != InventoryType.CHEST)
            return false;

        int[] slots = {18, 19, 20, 21, 22, 23, 24, 25, 26};
        boolean isSorter = false;

        for (int i : slots) {
            ItemStack temp = inv.getItem(i);
            
            if (temp != null && temp.equals(SorterManager.getSorterWall()))
                isSorter = true;
        }

        return isSorter;
    }

    /**
     * Returns the custom name of a chest or double chest, if either side has one. Prefers the left chest.
     * @param block The block to get the name from
     * @return Optional containing the name of the block.
     */
    public static Optional<String> getChestCustomName(Block block) {
        Chest chest = (Chest) block.getState();
        if (chest.getInventory().getHolder() instanceof DoubleChest) {
            DoubleChest doubleChest = (DoubleChest) chest.getInventory().getHolder();
            Chest leftChest = (Chest) doubleChest.getLeftSide();
            Chest rightChest = (Chest) doubleChest.getRightSide();
            if (leftChest.getCustomName() != null) {
                return Optional.of(leftChest.getCustomName());
            }
            if (rightChest.getCustomName() != null) {
                return Optional.of(rightChest.getCustomName());
            }
        }
        return Optional.ofNullable(chest.getCustomName());
    }
}
