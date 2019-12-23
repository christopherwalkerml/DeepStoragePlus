package me.darkolythe.deepstorageplus;

import me.darkolythe.customrecipeapi.APIManager;
import me.darkolythe.customrecipeapi.CustomRecipe;
import me.darkolythe.customrecipeapi.CustomRecipeAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RecipeManager {

    private DeepStoragePlus main;

    public RecipeManager(DeepStoragePlus plugin) {
        this.main = plugin; // set it equal to an instance of main

        createWorkbench();
        createStorages();
    }

    private void createWorkbench() {
        ItemStack workbenchItem = new ItemStack(Material.DISPENSER);
        ItemMeta meta = workbenchItem.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Special Crafting");
        workbenchItem.setItemMeta(meta);
        ShapedRecipe workbenchRecipe = new ShapedRecipe(new NamespacedKey(main, "Workbench"), workbenchItem);
        workbenchRecipe.shape("IGI", "GDG", "IGI");
        workbenchRecipe.setIngredient('I', Material.IRON_BLOCK);
        workbenchRecipe.setIngredient('G', Material.GLASS);
        workbenchRecipe.setIngredient('D', Material.DIAMOND_BLOCK);
        Bukkit.getServer().addRecipe(workbenchRecipe);

        main.crapimanager.setWorkBench(workbenchRecipe);
    }

    private void createStorages() {
        ItemStack redstone = new ItemStack(Material.REDSTONE);
        ItemStack quartz = new ItemStack(Material.QUARTZ);
        ItemStack gold = new ItemStack(Material.GOLD_INGOT);
        ItemStack iron = new ItemStack(Material.IRON_INGOT);
        ItemStack glass = new ItemStack(Material.GLASS);
        ItemStack glowstone = new ItemStack(Material.GLOWSTONE_DUST);
        ItemStack diamond = new ItemStack(Material.DIAMOND);
        ItemStack emerald = new ItemStack(Material.EMERALD);


        ItemStack storageCell1K = createStorageCell(4, ChatColor.WHITE.toString() + "Storage Cell " + ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + "1K");

        ItemStack storageCell4K = createStorageCell(7, ChatColor.WHITE.toString() + "Storage Cell " + ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "4K");

        ItemStack storageCell16K = createStorageCell(10, ChatColor.WHITE.toString() + "Storage Cell " + ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "16K");

        ItemStack storageCell64K = createStorageCell(13, ChatColor.WHITE.toString() + "Storage Cell " + ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "64K");

        ItemStack storageCell256K = createStorageCell(16, ChatColor.WHITE.toString() + "Storage Cell " + ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + "256K");


        ItemStack storageContainer1K = createStorageCell(20, ChatColor.WHITE.toString() + "Storage Container " + ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + "1K");
        createLore(storageContainer1K, 1024);

        ItemStack storageContainer4K = createStorageCell(23, ChatColor.WHITE.toString() + "Storage Container " + ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "4K");
        createLore(storageContainer4K, 1024 * 4);

        ItemStack storageContainer16K = createStorageCell(26, ChatColor.WHITE.toString() + "Storage Container " + ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "16K");
        createLore(storageContainer16K, 1024 * 16);

        ItemStack storageContainer64K = createStorageCell(29, ChatColor.WHITE.toString() + "Storage Container " + ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "64K");
        createLore(storageContainer64K, 1024 * 64);

        ItemStack storageContainer256K = createStorageCell(32, ChatColor.WHITE.toString() + "Storage Container " + ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "256K");
        createLore(storageContainer256K, 1024 * 256);

        CustomRecipeAPI.createRecipe(storageCell1K, redstone, quartz, redstone, quartz, gold, quartz, redstone, quartz, redstone);
        CustomRecipeAPI.createRecipe(storageCell4K, redstone, iron, redstone, storageCell1K, glass, storageCell1K, redstone, storageCell1K, redstone);
        CustomRecipeAPI.createRecipe(storageCell16K, glowstone, diamond, glowstone, storageCell4K, glass, storageCell4K, glowstone, storageCell4K, glowstone);
        CustomRecipeAPI.createRecipe(storageCell64K, glowstone, diamond, glowstone, storageCell16K, glass, storageCell16K, glowstone, storageCell16K, glowstone);
        CustomRecipeAPI.createRecipe(storageCell256K, emerald, diamond, emerald, storageCell64K, glass, storageCell64K, emerald, storageCell64K, emerald);

        CustomRecipeAPI.createRecipe(storageContainer1K, glass, redstone, glass, redstone, storageCell1K, redstone, iron, iron, iron);
        CustomRecipeAPI.createRecipe(storageContainer4K, glass, redstone, glass, redstone, storageCell4K, redstone, iron, iron, iron);
        CustomRecipeAPI.createRecipe(storageContainer16K, glass, redstone, glass, redstone, storageCell16K, redstone, gold, gold, gold);
        CustomRecipeAPI.createRecipe(storageContainer64K, glass, redstone, glass, redstone, storageCell64K, redstone, gold, gold, gold);
        CustomRecipeAPI.createRecipe(storageContainer256K, glass, redstone, glass, redstone, storageCell256K, redstone, diamond, diamond, diamond);
    }

    private static ItemStack createStorageCell(int durability, String str) {
        ItemStack storageCell = new ItemStack(Material.STONE_AXE);
        ItemMeta storageCellMeta = storageCell.getItemMeta();
        storageCellMeta.setDisplayName(str);
        storageCellMeta.setUnbreakable(true);
        storageCellMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        storageCellMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        storageCell.setItemMeta(storageCellMeta);
        storageCell.setDurability((short) durability);

        return storageCell;
    }

    private static void createLore(ItemStack container, int storageMax) {
        int maxTypes = 5;
        List<String> lore = new ArrayList<>();

        ItemMeta meta = container.getItemMeta();
        lore.add(ChatColor.GREEN + "Current Storage: " + 0 + "/" + storageMax);

        lore.add(ChatColor.GREEN + "Current Types: " + 0 + "/" + maxTypes);

        for (int i = 0; i < maxTypes; i++) {
            lore.add(ChatColor.WHITE + " - " + "empty");
        }

        meta.setLore(lore);
        container.setItemMeta(meta);
    }
}
