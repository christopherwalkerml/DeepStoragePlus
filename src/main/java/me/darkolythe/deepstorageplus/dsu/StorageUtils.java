package me.darkolythe.deepstorageplus.dsu;

import me.darkolythe.deepstorageplus.dsu.managers.DSUManager;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class StorageUtils {

    /*
    Check if the item "has no meta" which counts enchants, damage, lore, name, etc.
     */
    public static boolean hasNoMeta(ItemStack item) {
        if (item.getDurability() != 0) {
            return false;
        }
        if (item.getEnchantments().size() > 0) {
            return false;
        }
        if (item.getType().toString().contains("SHULKER_BOX")) {
            return false;
        }
        if (item.hasItemMeta()) {
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

        int slots[] = {7, 16, 25, 34, 43, 52};

        for (int i : slots) {
            if (inv.getItem(i) == null || !inv.getItem(i).equals(DSUManager.getDSUWall()))
                return false;
        }

        return true;
    }
}
