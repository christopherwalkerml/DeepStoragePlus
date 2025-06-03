package me.darkolythe.deepstorageplus.utils;

import me.darkolythe.deepstorageplus.DeepStoragePlus;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

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

    public Map<String, ItemStack> itemListMap = new HashMap<>();

    public ItemList(DeepStoragePlus plugin) {
        this.main = plugin; // set it equal to an instance of main

        // Item Definitions
        this.storageCell1K = createStorageCell(13010, ChatColor.WHITE + LanguageManager.getValue("storagecell") + " " + ChatColor.GRAY + ChatColor.BOLD + "1K");

        this.storageCell4K = createStorageCell(13011, ChatColor.WHITE + LanguageManager.getValue("storagecell") + " " + ChatColor.WHITE + ChatColor.BOLD + "4K");

        this.storageCell16K = createStorageCell(13012, ChatColor.WHITE + LanguageManager.getValue("storagecell") + " " + ChatColor.YELLOW + ChatColor.BOLD + "16K");

        this.storageCell64K = createStorageCell(13013, ChatColor.WHITE + LanguageManager.getValue("storagecell") + " " + ChatColor.GREEN + ChatColor.BOLD + "64K");

        this.storageCell256K = createStorageCell(13014, ChatColor.WHITE + LanguageManager.getValue("storagecell") + " " + ChatColor.BLUE + ChatColor.BOLD + "256K");

        this.storageCell1M = createStorageCell(13015, ChatColor.WHITE + LanguageManager.getValue("storagecell") + " " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "1M");

        this.storageContainer1K = createStorageCell(13016, ChatColor.WHITE + LanguageManager.getValue("storagecontainer") + " " + ChatColor.GRAY + ChatColor.BOLD + "1K");
        createLore(storageContainer1K, getStorageMaxConfig("1kmax"));

        this.storageContainer4K = createStorageCell(13017, ChatColor.WHITE + LanguageManager.getValue("storagecontainer") + " " + ChatColor.WHITE + ChatColor.BOLD + "4K");
        createLore(storageContainer4K, getStorageMaxConfig("4kmax"));

        this.storageContainer16K = createStorageCell(13018, ChatColor.WHITE + LanguageManager.getValue("storagecontainer") + " " + ChatColor.YELLOW + ChatColor.BOLD + "16K");
        createLore(storageContainer16K, getStorageMaxConfig("16kmax"));

        this.storageContainer64K = createStorageCell(13019, ChatColor.WHITE + LanguageManager.getValue("storagecontainer") + " " + ChatColor.GREEN + ChatColor.BOLD + "64K");
        createLore(storageContainer64K, getStorageMaxConfig("64kmax"));

        this.storageContainer256K = createStorageCell(13020, ChatColor.WHITE + LanguageManager.getValue("storagecontainer") + " " + ChatColor.BLUE + ChatColor.BOLD + "256K");
        createLore(storageContainer256K, this.getStorageMaxConfig("256kmax"));

        this.storageContainer1M = createStorageCell(13021, ChatColor.WHITE + LanguageManager.getValue("storagecontainer") + " " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "1M");
        createLore(storageContainer1M, getStorageMaxConfig("1mmax"));

        this.creativeStorageContainer = createStorageCell(13021, ChatColor.DARK_PURPLE + LanguageManager.getValue("creativestoragecontainer"));
        createLore(creativeStorageContainer, Integer.MAX_VALUE);

        this.storageWrench = createStorageWrench();
        this.sorterWrench = createSorterWrench();
        this.linkModule = createLinkModule();
        this.receiver = createReceiver();
        this.terminal = createTerminal();
        this.speedUpgrade = createSpeedUpgrade();

        itemListMap.put("storage_cell_1k", storageCell1K);
        itemListMap.put("storage_cell_4k", storageCell4K);
        itemListMap.put("storage_cell_16k", storageCell16K);
        itemListMap.put("storage_cell_64k", storageCell64K);
        itemListMap.put("storage_cell_256k", storageCell256K);
        itemListMap.put("storage_cell_1m", storageCell1M);
        itemListMap.put("storage_container_1k", storageContainer1K);
        itemListMap.put("storage_container_4k", storageContainer4K);
        itemListMap.put("storage_container_16k", storageContainer16K);
        itemListMap.put("storage_container_64k", storageContainer64K);
        itemListMap.put("storage_container_256k", storageContainer256K);
        itemListMap.put("storage_container_1m", storageContainer1M);
        itemListMap.put("creative_storage_container", creativeStorageContainer);
        itemListMap.put("storage_wrench", storageWrench);
        itemListMap.put("sorter_wrench", sorterWrench);
        itemListMap.put("receiver", receiver);
        itemListMap.put("terminal", terminal);
        itemListMap.put("speed_upgrade", speedUpgrade);
        itemListMap.put("link_module", linkModule);
    }

    // Helper methods
    public Optional<ItemStack> getItem(String itemName) {
        ItemStack item = null;
        if (itemName == null) {
            return Optional.empty();
        }
        if (itemListMap.containsKey(itemName)) {
            item = itemListMap.get(itemName);
        }
        return Optional.ofNullable(item);
    }

    private int getStorageMaxConfig(String size) {
        if (main.getConfig().getBoolean("countinstacks")) {
            return main.getConfig().getInt(size) * 1024 * 64;
        }
        return main.getConfig().getInt(size) * 1024;
    }

    private static ItemStack createStorageCell(int textureId, String name) {
        ItemStack storageCell = new ItemStack(Material.STONE_AXE);
        ItemMeta storageCellMeta = storageCell.getItemMeta();
        storageCellMeta.setDisplayName(name);
        // set texture data ID
        storageCellMeta.setCustomModelData(textureId);
        storageCell.setItemMeta(storageCellMeta);

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
        ItemStack storageWrench = createStorageCell(13003, ChatColor.AQUA + LanguageManager.getValue("storageloader"));
        storageWrench.setType(Material.STONE_AXE);
        ItemMeta wrenchmeta = storageWrench.getItemMeta();
        wrenchmeta.setLore(Arrays.asList(ChatColor.GRAY + LanguageManager.getValue("clickempty"),
                ChatColor.GRAY + LanguageManager.getValue("tocreatedsu"), "", ChatColor.GRAY + LanguageManager.getValue("onetimeuse")));
        storageWrench.setItemMeta(wrenchmeta);

        return storageWrench;
    }

    public static ItemStack createSorterWrench() {
        ItemStack sorterWrench = createStorageCell(13002, ChatColor.AQUA + LanguageManager.getValue("sorterloader"));
        sorterWrench.setType(Material.STONE_AXE);
        ItemMeta wrenchmeta = sorterWrench.getItemMeta();
        wrenchmeta.setLore(Arrays.asList(ChatColor.GRAY + LanguageManager.getValue("clickempty"),
                ChatColor.GRAY + LanguageManager.getValue("tocreatesorter"), "", ChatColor.GRAY + LanguageManager.getValue("onetimeuse")));
        sorterWrench.setItemMeta(wrenchmeta);

        return sorterWrench;
    }

    public static ItemStack createLinkModule() {
        ItemStack linkModule = createStorageCell(13004, ChatColor.AQUA + LanguageManager.getValue("linkmodule"));
        linkModule.setType(Material.STONE_AXE);
        ItemMeta wrenchmeta = linkModule.getItemMeta();
        wrenchmeta.setLore(Arrays.asList(ChatColor.GRAY + "Click DSU",
                ChatColor.GRAY + "To save DSU coordinates to this link module"));
        linkModule.setItemMeta(wrenchmeta);

        return linkModule;
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

    /**
     * Used to compare two ItemStacks from this mod. Returns true if they are of the same type, even if they have different lore.
     * @return true if the items are similar
     */
    public static boolean compareItem(ItemStack item1, ItemStack item2) {
        if (!item1.hasItemMeta() || !item2.hasItemMeta()) {
            return false;
        }

        return item1.getItemMeta().getDisplayName().equals(item2.getItemMeta().getDisplayName())
                && item1.getItemMeta().isUnbreakable() == item2.getItemMeta().isUnbreakable();
    }
}
