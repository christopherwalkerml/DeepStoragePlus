package me.darkolythe.deepstorageplus;

import me.darkolythe.customrecipeapi.CustomRecipeAPI;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class DeepStoragePlus extends JavaPlugin {

    public static String prefix = ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "[" + ChatColor.BLUE.toString() + "DeepStorage" + ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "] ";

    private InventoryListener inventorylistener;
    private ChestListener chestlistener;
    private RecipeManager recipemanager;
    public CustomRecipeAPI CRAPI;

    @Override
    public void onEnable() {
        CRAPI = new CustomRecipeAPI(this);
        inventorylistener = new InventoryListener(this);
        chestlistener = new ChestListener(this);
        recipemanager = new RecipeManager(this);

        getServer().getPluginManager().registerEvents(inventorylistener, this);
        getServer().getPluginManager().registerEvents(chestlistener, this);

        getCommand("multitool").setExecutor(new CommandHandler());

        System.out.println(prefix + ChatColor.GREEN + "DeepStoragePlus enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
