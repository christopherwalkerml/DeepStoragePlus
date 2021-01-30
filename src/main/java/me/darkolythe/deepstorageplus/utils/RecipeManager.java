package me.darkolythe.deepstorageplus.utils;

import me.darkolythe.customrecipeapi.APIManager;
import me.darkolythe.customrecipeapi.CustomRecipeAPI;
import me.darkolythe.deepstorageplus.DeepStoragePlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class RecipeManager {

    private DeepStoragePlus main;
    private ItemList itemList;

    public RecipeManager(DeepStoragePlus plugin, ItemList itemList) {
        this.main = plugin; // set it equal to an instance of main
        this.itemList = itemList;

        if (APIManager.getWorkbench() == null) {
            createWorkbench();
        }
        createStorages();
    }

    private void createWorkbench() {
        ItemStack workbenchItem = new ItemStack(Material.DISPENSER);
        ItemMeta meta = workbenchItem.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + LanguageManager.getValue("specialcrafting"));
        workbenchItem.setItemMeta(meta);
        ShapedRecipe workbenchRecipe = new ShapedRecipe(new NamespacedKey(main, "Workbench"), workbenchItem);
        workbenchRecipe.shape("IGI", "GDG", "IGI");
        workbenchRecipe.setIngredient('I', Material.IRON_BLOCK);
        workbenchRecipe.setIngredient('G', Material.GLASS);
        workbenchRecipe.setIngredient('D', Material.DIAMOND_BLOCK);
        Bukkit.getServer().addRecipe(workbenchRecipe);

        APIManager.setWorkBench(workbenchRecipe);
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
        ItemStack air = new ItemStack(Material.AIR);
        ItemStack diamondblock = new ItemStack(Material.DIAMOND_BLOCK);
        ItemStack redstoneblock = new ItemStack(Material.REDSTONE_BLOCK, 32);
        ItemStack endereye = new ItemStack(Material.ENDER_EYE);

        CustomRecipeAPI.createRecipe(itemList.storageCell1K, redstone, quartz, redstone, quartz, gold, quartz, redstone, quartz, redstone);
        CustomRecipeAPI.createRecipe(itemList.storageCell4K, redstone, iron, redstone, itemList.storageCell1K, glass, itemList.storageCell1K, redstone, itemList.storageCell1K, redstone);
        CustomRecipeAPI.createRecipe(itemList.storageCell16K, glowstone, diamond, glowstone, itemList.storageCell4K, glass, itemList.storageCell4K, glowstone, itemList.storageCell4K, glowstone);
        CustomRecipeAPI.createRecipe(itemList.storageCell64K, glowstone, diamond, glowstone, itemList.storageCell16K, glass, itemList.storageCell16K, glowstone, itemList.storageCell16K, glowstone);
        CustomRecipeAPI.createRecipe(itemList.storageCell256K, emerald, diamond, emerald, itemList.storageCell64K, glass, itemList.storageCell64K, emerald, itemList.storageCell64K, emerald);
        CustomRecipeAPI.createRecipe(itemList.storageCell1M, emerald, diamond, emerald, itemList.storageCell256K, glass, itemList.storageCell256K, emerald, itemList.storageCell256K, emerald);

        CustomRecipeAPI.createRecipe(itemList.storageContainer1K, glass, redstone, glass, redstone, itemList.storageCell1K, redstone, iron, iron, iron);
        CustomRecipeAPI.createRecipe(itemList.storageContainer4K, glass, redstone, glass, redstone, itemList.storageCell4K, redstone, iron, iron, iron);
        CustomRecipeAPI.createRecipe(itemList.storageContainer16K, glass, redstone, glass, redstone, itemList.storageCell16K, redstone, gold, gold, gold);
        CustomRecipeAPI.createRecipe(itemList.storageContainer64K, glass, redstone, glass, redstone, itemList.storageCell64K, redstone, gold, gold, gold);
        CustomRecipeAPI.createRecipe(itemList.storageContainer256K, glass, redstone, glass, redstone, itemList.storageCell256K, redstone, diamond, diamond, diamond);
        CustomRecipeAPI.createRecipe(itemList.storageContainer1M, glass, redstone, glass, redstone, itemList.storageCell1M, redstone, diamond, diamond, diamond);

        CustomRecipeAPI.createRecipe(itemList.sorterWrench, iron, air, iron, air, gold, air, iron, air, iron);
        CustomRecipeAPI.createRecipe(itemList.storageWrench, iron, air, iron, air, quartz, air, iron, air, iron);
        CustomRecipeAPI.createRecipe(itemList.linkModule, redstone, quartz, redstone, diamond, gold, diamond, redstone, quartz, redstone);
        CustomRecipeAPI.createRecipe(itemList.receiver, air, endereye, air, air, quartz, air, iron, diamond, iron);
        CustomRecipeAPI.createRecipe(itemList.terminal, air, itemList.receiver, air, redstoneblock, diamondblock, redstoneblock, diamond, quartz, diamond);
        CustomRecipeAPI.createRecipe(itemList.speedUpgrade, redstone, redstone, redstone, redstone, diamond, redstone, redstone, redstone, redstone);

    }
}
