package me.darkolythe.deepstorageplus;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StorageContainer extends ItemStack {

    int storageMax = 1;
    int storageCur = 0;
    int maxTypes = 5;
    ItemStack[] items = new ItemStack[maxTypes];

    public StorageContainer(Material material, String str) {
        setType(material);
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(str);
        setItemMeta(meta);

        setStorage(Integer.parseInt(str.replaceAll("/\\D/","")));
        setItems();
        updateLore();
    }

    private void setStorage(int newStorage) {
        storageMax = newStorage * 1024;
    }

    private void setItems() {
        ItemStack air = new ItemStack(Material.AIR);
        for (int i = 0; i < 5; i++) {
            items[i] = air;
        }
    }

    private void updateLore() {
        List<String> lore = new ArrayList<>();

        ItemMeta meta = getItemMeta();
        if (storageCur < storageMax / 2) {
            lore.add(ChatColor.GREEN + "Current Storage: " + storageCur + "/" + storageMax);
        } else if (storageCur > storageMax / 2 && storageCur < storageMax) {
            lore.add(ChatColor.YELLOW + "Current Storage: " + storageCur + "/" + storageMax);
        } else {
            lore.add(ChatColor.RED + "Current Storage: " + storageCur + "/" + storageMax);
        }

        if (countEmptyItems() < maxTypes / 2) {
            lore.add(ChatColor.GREEN + "Current Types: " + (maxTypes - countEmptyItems()) + "/" + maxTypes);
        } else if (countEmptyItems() < maxTypes - 1) {
            lore.add(ChatColor.YELLOW + "Current Types: " + (maxTypes + countEmptyItems()) + "/" + maxTypes);
        } else {
            lore.add(ChatColor.RED + "Current Types: " + (maxTypes + countEmptyItems()) + "/" + maxTypes);
        }

        for (int i = 0; i < maxTypes; i++) {
            if (items[i].getType() != Material.AIR) {
                lore.add(ChatColor.WHITE + " - " + WordUtils.capitalize(items[i].getType().toString().toLowerCase().replaceAll("_","")));
            }
        }

        meta.setLore(lore);
        setItemMeta(meta);
    }

    private int countEmptyItems() {
        int count = 0;
        for (int i = 0; i < maxTypes; i++) {
            if (items[i].getType() == Material.AIR) {
                count++;
            }
        }
        return count;
    }
}
