package me.darkolythe.deepstorageplus.utils;

import me.darkolythe.deepstorageplus.DeepStoragePlus;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static me.darkolythe.deepstorageplus.dsu.managers.WirelessManager.createReceiver;
import static me.darkolythe.deepstorageplus.dsu.managers.WirelessManager.createTerminal;

public class ItemList {

    private DeepStoragePlus main;

    public ItemStack storageCell1K;
    public ItemStack storageCell4K;
    public ItemStack storageCell16K;
    public ItemStack storageCell64K;
    public ItemStack storageCell256K;
    public ItemStack storageCell1M;
    public ItemStack storageContainer1K;
    public ItemStack storageContainer4K;
    public ItemStack storageContainer16K;
    public ItemStack storageContainer64K;
    public ItemStack storageContainer256K;
    public ItemStack storageContainer1M;
    public ItemStack creativeStorageContainer;
    public ItemStack storageWrench;
    public ItemStack sorterWrench;
    public ItemStack receiver;
    public ItemStack terminal;
    public ItemStack speedUpgrade;
    public ItemStack linkModule;



    public ItemList(DeepStoragePlus plugin) {
        this.main = plugin; // set it equal to an instance of main


        // Item Definitions
        this.storageCell1K = createStorageCell(15, ChatColor.WHITE.toString() + LanguageManager.getValue("storagecell") + " " + ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + "1K");

        this.storageCell4K = createStorageCell(30, ChatColor.WHITE.toString() + LanguageManager.getValue("storagecell") + " " + ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "4K");

        this.storageCell16K = createStorageCell(40, ChatColor.WHITE.toString() + LanguageManager.getValue("storagecell") + " " + ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "16K");

        this.storageCell64K = createStorageCell(53, ChatColor.WHITE.toString() + LanguageManager.getValue("storagecell") + " " + ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "64K");

        this.storageCell256K = createStorageCell(66, ChatColor.WHITE.toString() + LanguageManager.getValue("storagecell") + " " + ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + "256K");

        this.storageCell1M = createStorageCell(10, ChatColor.WHITE.toString() + LanguageManager.getValue("storagecell") + " " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "1M");

        this.storageContainer1K = createStorageCell(79, ChatColor.WHITE.toString() + LanguageManager.getValue("storagecontainer") + " " + ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + "1K");
        createLore(storageContainer1K, getStorageMaxConfig("1kmax"));

        this.storageContainer4K = createStorageCell(92, ChatColor.WHITE.toString() + LanguageManager.getValue("storagecontainer") + " " + ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "4K");
        createLore(storageContainer4K, getStorageMaxConfig("4kmax"));

        this.storageContainer16K = createStorageCell(105, ChatColor.WHITE.toString() + LanguageManager.getValue("storagecontainer") + " " + ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "16K");
        createLore(storageContainer16K, getStorageMaxConfig("16kmax"));

        this.storageContainer64K = createStorageCell(118, ChatColor.WHITE.toString() + LanguageManager.getValue("storagecontainer") + " " + ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "64K");
        createLore(storageContainer64K, getStorageMaxConfig("64kmax"));

        this.storageContainer256K = createStorageCell(130, ChatColor.WHITE.toString() + LanguageManager.getValue("storagecontainer") + " " + ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + "256K");
        createLore(storageContainer256K, this.getStorageMaxConfig("256kmax"));

        this.storageContainer1M = createStorageCell(20, ChatColor.WHITE.toString() + LanguageManager.getValue("storagecontainer") + " " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "1M");
        createLore(storageContainer1M, getStorageMaxConfig("1mmax"));

        this.creativeStorageContainer = createStorageCell(20, ChatColor.DARK_PURPLE.toString() + LanguageManager.getValue("creativestoragecontainer"));
        createLore(creativeStorageContainer, Integer.MAX_VALUE);

        this.storageWrench = createStorageWrench();
        this.sorterWrench = createSorterWrench();
        this.linkModule = createLinkModule();
        this.receiver = createReceiver();
        this.terminal = createTerminal();
        this.speedUpgrade = createSpeedUpgrade();
    }

    // Helper methods
    public Optional<ItemStack> getItem(String itemName) {
        ItemStack item = null;
        if (itemName == null) {
            return Optional.empty();
        }
        switch (itemName.toLowerCase()) {
            case "storagecell1k": item = storageCell1K; break;
            case "storagecell4k": item = storageCell4K; break;
            case "storagecell16k": item = storageCell16K; break;
            case "storagecell64k": item = storageCell64K; break;
            case "storagecell256k": item = storageCell256K; break;
            case "storagecell1m": item = storageCell1M; break;
            case "storagecontainer1k": item = storageContainer1K; break;
            case "storagecontainer4k": item = storageContainer4K; break;
            case "storagecontainer16k": item = storageContainer16K; break;
            case "storagecontainer64k": item = storageContainer64K; break;
            case "storagecontainer256k": item = storageContainer256K; break;
            case "storagecontainer1m": item = storageContainer1M; break;
            case "creativestoragecontainer": item = creativeStorageContainer; break;
            case "storagewrench": item = storageWrench; break;
            case "sorterwrench": item = sorterWrench; break;
            case "receiver": item = receiver; break;
            case "terminal": item = terminal; break;
            case "speedupgrade": item = speedUpgrade; break;
            case "linkmodule": item = linkModule; break;
        }
        return Optional.ofNullable(item);
    }

    private int getStorageMaxConfig(String size) {
        if (main.getConfig().getBoolean("countinstacks")) {
            return main.getConfig().getInt(size) * 1024 * 64;
        }
        return main.getConfig().getInt(size) * 1024;
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
        int maxTypes = 7;
        List<String> lore = new ArrayList<>();

        ItemMeta meta = container.getItemMeta();
        lore.add(ChatColor.GREEN + LanguageManager.getValue("currentstorage") + ": " + 0 + "/" + storageMax);

        lore.add(ChatColor.GREEN + LanguageManager.getValue("currenttypes") + ": " + 0 + "/" + maxTypes);

        for (int i = 0; i < maxTypes; i++) {
            lore.add(ChatColor.GRAY + " - " + LanguageManager.getValue("empty"));
        }

        meta.setLore(lore);
        container.setItemMeta(meta);
    }

    public static ItemStack createStorageWrench() {
        ItemStack storageWrench = createStorageCell(130, ChatColor.AQUA.toString() + LanguageManager.getValue("storageloader"));
        storageWrench.setType(Material.STONE_SHOVEL);
        ItemMeta wrenchmeta = storageWrench.getItemMeta();
        wrenchmeta.setLore(Arrays.asList(ChatColor.GRAY + LanguageManager.getValue("clickempty"),
                ChatColor.GRAY + LanguageManager.getValue("tocreatedsu"), "", ChatColor.GRAY + LanguageManager.getValue("onetimeuse")));
        storageWrench.setItemMeta(wrenchmeta);

        return storageWrench;
    }

    public static ItemStack createLinkModule() {
        ItemStack linkModule = createStorageCell(98, ChatColor.AQUA.toString() + LanguageManager.getValue("linkmodule"));
        linkModule.setType(Material.STONE_SHOVEL);
        ItemMeta wrenchmeta = linkModule.getItemMeta();
        // TODO: We need to store data in the lore
        wrenchmeta.setLore(Arrays.asList(ChatColor.GRAY + LanguageManager.getValue("clickempty"),
                ChatColor.GRAY + LanguageManager.getValue("tocreatedsu"), "", ChatColor.GRAY + LanguageManager.getValue("onetimeuse")));
        linkModule.setItemMeta(wrenchmeta);

        return linkModule;
    }

    public static ItemStack createSorterWrench() {
        ItemStack sorterWrench = createStorageCell(105, ChatColor.AQUA.toString() + LanguageManager.getValue("sorterloader"));
        sorterWrench.setType(Material.STONE_SHOVEL);
        ItemMeta wrenchmeta = sorterWrench.getItemMeta();
        wrenchmeta.setLore(Arrays.asList(ChatColor.GRAY + LanguageManager.getValue("clickempty"),
                ChatColor.GRAY + LanguageManager.getValue("tocreatedsu"), "", ChatColor.GRAY + LanguageManager.getValue("onetimeuse")));
        sorterWrench.setItemMeta(wrenchmeta);

        return sorterWrench;
    }

    public static ItemStack createSpeedUpgrade() {
        ItemStack item = new ItemStack(Material.GLOWSTONE_DUST);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + LanguageManager.getValue("ioupgrade"));
        meta.setLore(Arrays.asList(ChatColor.GRAY + LanguageManager.getValue("clicktoupgrade")));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);

        return item;
    }
}
