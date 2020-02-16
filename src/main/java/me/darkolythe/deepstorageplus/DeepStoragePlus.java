package me.darkolythe.deepstorageplus;

import me.darkolythe.customrecipeapi.APIManager;
import me.darkolythe.customrecipeapi.CustomRecipeAPI;
import me.darkolythe.deepstorageplus.dsu.listeners.*;
import me.darkolythe.deepstorageplus.dsu.managers.DSUManager;
import me.darkolythe.deepstorageplus.dsu.managers.DSUUpdateManager;
import me.darkolythe.deepstorageplus.io.CommandHandler;
import me.darkolythe.deepstorageplus.io.ConfigManager;
import me.darkolythe.deepstorageplus.utils.*;
import org.bstats.bukkit.Metrics;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class DeepStoragePlus extends JavaPlugin {

    private static DeepStoragePlus plugin;

    public static String prefix;
    public static boolean loadpack;
    public static String DSUname = ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + "Deep Storage Unit";

    /*Currently open DSU for each player*/
    public static Map<UUID, Container> openDSU = new HashMap<>();
    /*Currently or last open DSU for each player*/
    public static Map<UUID, Inventory> stashedDSU = new HashMap<>();
    /*Buffered IO inventory, used for getting chat when adding players to lock*/
    public static Map<UUID, Inventory> stashedIO = new HashMap<>();
    /*Boolean for if the user is getting check for user input*/
    public static Map<UUID, Boolean> gettingInput = new HashMap<>();
    /*Boolean for IO inventory to be opened for player*/
    public static Map<Player, Boolean> openIOInv = new HashMap<>();
    /*Chunk loaded for players opening DSUs far away*/
    public static Map<Player, Chunk> loadedChunks = new HashMap<>();

    private InventoryListener inventorylistener;
    private WrenchListener wrenchlistener;
    private WirelessListener wirelesslistener;
    private IOListener iolistener;
    private StorageBreakListener storagebreakslistener;
    private RecipeManager recipemanager;
    public DSUUpdateManager dsuupdatemanager;
    public DSUManager dsumanager;
    public APIManager crapimanager;
    ConfigManager configmanager;

    public static int maxTypes = 7;

    @Override
    public void onEnable() {
        plugin = this;

        saveDefaultConfig();
        prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix")) + " ";
        loadpack = getConfig().getBoolean("loadresourcepack");

        LanguageManager.setup(plugin);

        inventorylistener = new InventoryListener(plugin);
        wrenchlistener = new WrenchListener(plugin);
        wirelesslistener = new WirelessListener(plugin);
        iolistener = new IOListener(plugin);
        storagebreakslistener = new StorageBreakListener(plugin);
        recipemanager = new RecipeManager(plugin);
        configmanager = new ConfigManager(plugin);
        dsuupdatemanager = new DSUUpdateManager(plugin);
        dsumanager = new DSUManager(plugin);
        crapimanager = CustomRecipeAPI.getManager();

        inventorylistener.addText();

        IDLibrary.initIDs();

        getServer().getPluginManager().registerEvents(inventorylistener, plugin);
        getServer().getPluginManager().registerEvents(wrenchlistener, plugin);
        getServer().getPluginManager().registerEvents(wirelesslistener, plugin);
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

    public static DeepStoragePlus getInstance() {
        return plugin;
    }
}
