package me.darkolythe.deepstorageplus;

import me.darkolythe.customrecipeapi.APIManager;
import me.darkolythe.customrecipeapi.CustomRecipeAPI;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.block.Container;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class DeepStoragePlus extends JavaPlugin {

    private static DeepStoragePlus plugin;

    public static String prefix = ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "[" + ChatColor.BLUE.toString() + "DeepStorage" + ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "] ";
    public static String DSUname = ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + "Deep Storage Unit";

    public static Map<UUID, Container> openDSU = new HashMap<>();

    private InventoryListener inventorylistener;
    private WrenchListener chestlistener;
    private IOListener iolistener;
    private RecipeManager recipemanager;
    public APIManager crapimanager;
    public ConfigManager configmanager;

    public static int maxTypes = 7;

    @Override
    public void onEnable() {
        plugin = this;

        inventorylistener = new InventoryListener(plugin);
        chestlistener = new WrenchListener();
        iolistener = new IOListener(plugin);
        recipemanager = new RecipeManager(plugin);
        configmanager = new ConfigManager(plugin);
        crapimanager = CustomRecipeAPI.getManager();

        getServer().getPluginManager().registerEvents(inventorylistener, plugin);
        getServer().getPluginManager().registerEvents(chestlistener, plugin);
        getServer().getPluginManager().registerEvents(configmanager, plugin);

        getCommand("deepstorageplus").setExecutor(new CommandHandler());

        Metrics metrics = new Metrics(plugin);

        System.out.println(prefix + ChatColor.GREEN + "DeepStoragePlus enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static DeepStoragePlus getInstance() {
        return plugin;
    }
}
