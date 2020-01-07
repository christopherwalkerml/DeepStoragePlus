package me.darkolythe.deepstorageplus;

import org.bukkit.inventory.ItemStack;

public class StorageUtils {

    public static boolean hasNoMeta(ItemStack item) {
        if (item.hasItemMeta()) {
            return false;
        } else if (item.getDurability() != 0) {
            return false;
        } else if (item.getEnchantments().size() > 0) {
            return false;
        }
        return true;
    }

}
