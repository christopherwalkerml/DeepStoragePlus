package me.darkolythe.deepstorageplus;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class StorageUtils {

    /*
    Check if the item "has no meta" which counts enchants, damage, lore, name, etc.
     */
    static boolean hasNoMeta(ItemStack item) {
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
    Turns a Material into a String. ex: EMERALD_ORE -> Emerald Ore
     */
    static String matToString(Material mat) {
        return WordUtils.capitalize(mat.toString().toLowerCase().replaceAll("_", " "));
    }

    /*
    Turns a String into a Material. ex: Emerald Ore -> EMERALD_ORE
     */
    static Material stringToMat(String str, String remStr) {
        return Material.valueOf(str.replace(remStr, "").toUpperCase().replace(" ", "_").toUpperCase());
    }
}
