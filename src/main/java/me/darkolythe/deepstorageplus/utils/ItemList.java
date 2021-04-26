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

    public Map<String, ItemStack> itemListMap = new HashMap<String, ItemStack>()
    {{
        put("storagecell1k", storageCell1K);
        put("storagecell4k", storageCell4K);
        put("storagecell16k", storageCell16K);
        put("storagecell64k", storageCell64K);
        put("storagecell256k", storageCell256K);
        put("storagecell1m", storageCell1M);
        put("storagecontainer1k", storageContainer1K);
        put("storagecontainer4k", storageContainer4K);
        put("storagecontainer16k", storageContainer16K);
        put("storagecontainer64k", storageContainer64K);
        put("storagecontainer256k", storageContainer256K);
        put("storagecontainer1m", storageContainer1M);
        put("creativestoragecontainer", creativeStorageContainer);
        put("storagewrench", storageWrench);
        put("sorterwrench", sorterWrench);
        put("receiver", receiver);
        put("terminal", terminal);
        put("speedupgrade", speedUpgrade);
        put("linkmodule", linkModule);
    }};

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

    private static ItemStack createStorageCell(int durability, String name) {
        ItemStack storageCell = new ItemStack(Material.STONE_AXE);
        ItemMeta storageCellMeta = storageCell.getItemMeta();
        storageCellMeta.setDisplayName(name);
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

    public static ItemStack createSorterWrench() {
        ItemStack sorterWrench = createStorageWrench();
        sorterWrench.setDurability((short)105);
        ItemMeta wrenchmeta = sorterWrench.getItemMeta();
        wrenchmeta.setDisplayName(ChatColor.AQUA.toString() + LanguageManager.getValue("sorterloader"));
        sorterWrench.setItemMeta(wrenchmeta);

        return sorterWrench;
    }

    public static ItemStack createLinkModule() {
        ItemStack linkModule = createStorageCell(99, ChatColor.AQUA.toString() + LanguageManager.getValue("linkmodule"));
        linkModule.setType(Material.STONE_SHOVEL);
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
