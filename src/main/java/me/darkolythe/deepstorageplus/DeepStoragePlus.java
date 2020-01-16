package me.darkolythe.deepstorageplus;

import me.darkolythe.customrecipeapi.APIManager;
import me.darkolythe.customrecipeapi.CustomRecipeAPI;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.block.Container;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class DeepStoragePlus extends JavaPlugin {

    private static DeepStoragePlus plugin;

    static String prefix;
    static boolean loadpack;
    static String DSUname = ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + "Deep Storage Unit";

    static Map<UUID, Container> openDSU = new HashMap<>();
    static Map<UUID, Inventory> stashedDSU = new HashMap<>();

    private InventoryListener inventorylistener;
    private WrenchListener wrenchlistener;
    private IOListener iolistener;
    private StorageBreakListener storagebreakslistener;
    private RecipeManager recipemanager;
    DSUUpdateManager dsuupdatemanager;
    DSUManager dsumanager;
    APIManager crapimanager;
    ConfigManager configmanager;

    static int maxTypes = 7;

    @Override
    public void onEnable() {
        plugin = this;

        saveDefaultConfig();
        prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix")) + " ";
        loadpack = getConfig().getBoolean("loadresourcepack");

        LanguageManager.setup(plugin);

        inventorylistener = new InventoryListener(plugin);
        wrenchlistener = new WrenchListener(plugin);
        iolistener = new IOListener(plugin);
        storagebreakslistener = new StorageBreakListener(plugin);
        recipemanager = new RecipeManager(plugin);
        configmanager = new ConfigManager(plugin);
        dsuupdatemanager = new DSUUpdateManager(plugin);
        dsumanager = new DSUManager(plugin);
        crapimanager = CustomRecipeAPI.getManager();

        IDLibrary.initIDs();

        getServer().getPluginManager().registerEvents(inventorylistener, plugin);
        getServer().getPluginManager().registerEvents(wrenchlistener, plugin);
        getServer().getPluginManager().registerEvents(iolistener, plugin);
        getServer().getPluginManager().registerEvents(storagebreakslistener, plugin);
        getServer().getPluginManager().registerEvents(configmanager, plugin);

        getCommand("deepstorageplus").setExecutor(new CommandHandler());

        Metrics metrics = new Metrics(plugin);

        System.out.println(prefix + ChatColor.GREEN + "DeepStoragePlus enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    static DeepStoragePlus getInstance() {
        return plugin;
    }
}
