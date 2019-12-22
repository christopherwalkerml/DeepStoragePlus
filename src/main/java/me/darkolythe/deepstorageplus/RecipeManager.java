package me.darkolythe.deepstorageplus;

import me.darkolythe.customrecipeapi.CustomRecipe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

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


        ItemStack storageCell1K = new ItemStack(Material.IRON_NUGGET);
        ItemMeta storageCell1KMeta = storageCell1K.getItemMeta();
        storageCell1KMeta.setDisplayName(ChatColor.WHITE + "Storage Cell " + ChatColor.GRAY.toString() + "1K");
        storageCell1K.setItemMeta(storageCell1KMeta);

        ItemStack storageCell4K = new ItemStack(Material.IRON_NUGGET);
        ItemMeta storageCell4KMeta = storageCell4K.getItemMeta();
        storageCell4KMeta.setDisplayName(ChatColor.WHITE + "Storage Cell " + ChatColor.WHITE.toString() + "4K");
        storageCell4K.setItemMeta(storageCell4KMeta);

        ItemStack storageCell16K = new ItemStack(Material.IRON_NUGGET);
        ItemMeta storageCell16KMeta = storageCell16K.getItemMeta();
        storageCell16KMeta.setDisplayName(ChatColor.WHITE + "Storage Cell " + ChatColor.YELLOW.toString() + "16K");
        storageCell16K.setItemMeta(storageCell16KMeta);

        ItemStack storageCell64K = new ItemStack(Material.IRON_INGOT);
        ItemMeta storageCell64KMeta = storageCell64K.getItemMeta();
        storageCell64KMeta.setDisplayName(ChatColor.WHITE + "Storage Cell " + ChatColor.GREEN.toString() + "64K");
        storageCell64K.setItemMeta(storageCell64KMeta);

        ItemStack storageCell256K = new ItemStack(Material.IRON_INGOT);
        ItemMeta storageCell256KMeta = storageCell256K.getItemMeta();
        storageCell256KMeta.setDisplayName(ChatColor.WHITE + "Storage Cell " + ChatColor.BLUE.toString() + "256K");
        storageCell256K.setItemMeta(storageCell256KMeta);


        StorageContainer storageContainer1K = new StorageContainer(Material.IRON_BLOCK, ChatColor.WHITE + "Storage Container " + ChatColor.GRAY.toString() + "1K");

        StorageContainer storageContainer4K = new StorageContainer(Material.IRON_BLOCK, ChatColor.WHITE + "Storage Container " + ChatColor.WHITE.toString() + "4K");

        StorageContainer storageContainer16K = new StorageContainer(Material.IRON_BLOCK, ChatColor.WHITE + "Storage Container " + ChatColor.YELLOW.toString() + "16K");

        StorageContainer storageContainer64K = new StorageContainer(Material.DIAMOND_BLOCK, ChatColor.WHITE + "Storage Container " + ChatColor.GREEN.toString() + "64K");

        StorageContainer storageContainer256K = new StorageContainer(Material.DIAMOND_BLOCK, ChatColor.WHITE + "Storage Container " + ChatColor.GREEN.toString() + "256K");

        CustomRecipe storageCell1KRecipe = new CustomRecipe(storageCell1K, redstone, quartz, redstone, quartz, gold, quartz, redstone, quartz, redstone);
        CustomRecipe storageCell4KRecipe = new CustomRecipe(storageCell4K, redstone, iron, redstone, storageCell1K, glass, storageCell1K, redstone, storageCell1K, redstone);
        CustomRecipe storageCell16KRecipe = new CustomRecipe(storageCell16K, glowstone, diamond, glowstone, storageCell4K, glass, storageCell4K, glowstone, storageCell4K, glowstone);
        CustomRecipe storageCell64KRecipe = new CustomRecipe(storageCell64K, glowstone, diamond, glowstone, storageCell16K, glass, storageCell16K, glowstone, storageCell16K, glowstone);
        CustomRecipe storageCell256KRecipe = new CustomRecipe(storageCell256K, emerald, diamond, emerald, storageCell64K, glass, storageCell64K, emerald, storageCell64K, emerald);

        CustomRecipe storageContainer1KRecipe = new CustomRecipe(storageContainer1K, glass, redstone, glass, redstone, storageCell1K, redstone, iron, iron, iron);
        CustomRecipe storageContainer4KRecipe = new CustomRecipe(storageContainer4K, glass, redstone, glass, redstone, storageCell4K, redstone, iron, iron, iron);
        CustomRecipe storageContainer16KRecipe = new CustomRecipe(storageContainer16K, glass, redstone, glass, redstone, storageCell16K, redstone, iron, iron, iron);
        CustomRecipe storageContainer64KRecipe = new CustomRecipe(storageContainer64K, glass, redstone, glass, redstone, storageCell64K, redstone, iron, iron, iron);
        CustomRecipe storageContainer256KRecipe = new CustomRecipe(storageContainer256K, glass, redstone, glass, redstone, storageCell256K, redstone, iron, iron, iron);

        main.crapimanager.addRecipe(storageCell1KRecipe);
        main.crapimanager.addRecipe(storageCell4KRecipe);
        main.crapimanager.addRecipe(storageCell16KRecipe);
        main.crapimanager.addRecipe(storageCell64KRecipe);
        main.crapimanager.addRecipe(storageCell256KRecipe);

        main.crapimanager.addRecipe(storageContainer1KRecipe);
        main.crapimanager.addRecipe(storageContainer4KRecipe);
        main.crapimanager.addRecipe(storageContainer16KRecipe);
        main.crapimanager.addRecipe(storageContainer64KRecipe);
        main.crapimanager.addRecipe(storageContainer256KRecipe);
    }
}
