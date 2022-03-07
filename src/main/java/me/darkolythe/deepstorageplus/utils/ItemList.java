package me.darkolythe.deepstorageplus.utils;

import me.darkolythe.deepstorageplus.DeepStoragePlus;
import me.darkolythe.deepstorageplus.utils.item.misc.LinkModule;
import me.darkolythe.deepstorageplus.utils.item.misc.SpeedUpgrade;
import me.darkolythe.deepstorageplus.utils.item.misc.WirelessReceiver;
import me.darkolythe.deepstorageplus.utils.item.misc.WirelessTerminal;
import me.darkolythe.deepstorageplus.utils.item.storagecells.StorageCell;
import me.darkolythe.deepstorageplus.utils.item.storagecontainer.StorageContainer;
import me.darkolythe.deepstorageplus.utils.item.wrench.SorterWrench;
import me.darkolythe.deepstorageplus.utils.item.wrench.StorageWrench;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ItemList {

    private DeepStoragePlus main;
    
    // Wrenches
    public static final int SORTER_WRENCH_MODEL_ID  = 20000;
    public static final int STORAGE_WRENCH_MODEL_ID = 20001;
    
    // Misc
    public static final int GUI_BACKGROUND_MODEL_ID = 20002;
    public static final int LINK_MODULE_MODEL_ID    = 20003;
    public static final int LINK_SLOT_MODEL_ID      = 20004;
    public static final int SPEEDUPGRADE_MODEL_ID   = 20005;
    public static final int STORAGE_SLOT_MODEL_ID   = 20006;
    public static final int RECEIVER_MODEL_ID       = 20007;
    public static final int TERMINAL_MODEL_ID       = 20008;
    
    // Storage Cells
    public static final int STORAGE_CELL_1K_MODEL_ID   = 20009;
    public static final int STORAGE_CELL_4K_MODEL_ID   = 20010;
    public static final int STORAGE_CELL_16K_MODEL_ID  = 20011;
    public static final int STORAGE_CELL_64K_MODEL_ID  = 20012;
    public static final int STORAGE_CELL_256K_MODEL_ID = 20013;
    public static final int STORAGE_CELL_1M_MODEL_ID   = 20014;
    
    // Storage Containers
    public static final int STORAGE_CONTAINER_1K_MODEL_ID   = 20015;
    public static final int STORAGE_CONTAINER_4K_MODEL_ID   = 20016;
    public static final int STORAGE_CONTAINER_16K_MODEL_ID  = 20017;
    public static final int STORAGE_CONTAINER_64K_MODEL_ID  = 20018;
    public static final int STORAGE_CONTAINER_256K_MODEL_ID = 20019;
    public static final int STORAGE_CONTAINER_1M_MODEL_ID   = 20020;

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
    private static final List<Integer> storageContainerIds = new ArrayList<>();

    public ItemList(DeepStoragePlus plugin) {
        this.main = plugin; // set it equal to an instance of main

        // Storage Cells
        this.storageCell1K = new StorageCell()
            .setCustomModelData(STORAGE_CELL_1K_MODEL_ID)
            .setName(getStorageCellName(ChatColor.WHITE, "storagecell", ChatColor.GRAY, "1K"))
            .setItemMeta()
            .getItem();
        
        this.storageCell4K = new StorageCell()
            .setCustomModelData(STORAGE_CELL_4K_MODEL_ID)
            .setName(getStorageCellName(ChatColor.WHITE, "storagecell", ChatColor.WHITE, "4K"))
            .setItemMeta()
            .getItem();
        
        this.storageCell16K = new StorageCell()
            .setCustomModelData(STORAGE_CELL_16K_MODEL_ID)
            .setName(getStorageCellName(ChatColor.WHITE, "storagecell", ChatColor.YELLOW, "16K"))
            .setItemMeta()
            .getItem();
        
        this.storageCell64K = new StorageCell()
            .setCustomModelData(STORAGE_CELL_64K_MODEL_ID)
            .setName(getStorageCellName(ChatColor.WHITE, "storagecell", ChatColor.GREEN, "64K"))
            .setItemMeta()
            .getItem();
        
        this.storageCell256K = new StorageCell()
            .setCustomModelData(STORAGE_CELL_256K_MODEL_ID)
            .setName(getStorageCellName(ChatColor.WHITE, "storagecell", ChatColor.BLUE, "256K"))
            .setItemMeta()
            .getItem();
        
        this.storageCell1M = new StorageCell()
            .setCustomModelData(STORAGE_CELL_1M_MODEL_ID)
            .setName(getStorageCellName(ChatColor.WHITE, "storagecell", ChatColor.LIGHT_PURPLE, "1M"))
            .setItemMeta()
            .getItem();
        
        // Storage Containers
        this.storageContainer1K = new StorageContainer()
            .setCustomModelData(STORAGE_CONTAINER_1K_MODEL_ID)
            .setName(getStorageCellName(ChatColor.WHITE, "storagecontainer", ChatColor.GRAY, "1K"))
            .setLore(getStorageMaxConfig("1kmax"))
            .setItemMeta()
            .getItem();
        
        this.storageContainer4K = new StorageContainer()
            .setCustomModelData(STORAGE_CONTAINER_4K_MODEL_ID)
            .setName(getStorageCellName(ChatColor.WHITE, "storagecontainer", ChatColor.WHITE, "4K"))
            .setLore(getStorageMaxConfig("4kmax"))
            .setItemMeta()
            .getItem();
        
        this.storageContainer16K = new StorageContainer()
            .setCustomModelData(STORAGE_CONTAINER_16K_MODEL_ID)
            .setName(getStorageCellName(ChatColor.WHITE, "storagecontainer", ChatColor.YELLOW, "16K"))
            .setLore(getStorageMaxConfig("16kmax"))
            .setItemMeta()
            .getItem();
        
        this.storageContainer64K = new StorageContainer()
            .setCustomModelData(STORAGE_CONTAINER_64K_MODEL_ID)
            .setName(getStorageCellName(ChatColor.WHITE, "storagecontainer", ChatColor.GREEN, "64K"))
            .setLore(getStorageMaxConfig("64kmax"))
            .setItemMeta()
            .getItem();
        
        this.storageContainer256K = new StorageContainer()
            .setCustomModelData(STORAGE_CONTAINER_256K_MODEL_ID)
            .setName(getStorageCellName(ChatColor.WHITE, "storagecontainer", ChatColor.BLUE, "256K"))
            .setLore(getStorageMaxConfig("256kmax"))
            .setItemMeta()
            .getItem();
        
        this.storageContainer1M = new StorageContainer()
            .setCustomModelData(STORAGE_CONTAINER_1M_MODEL_ID)
            .setName(getStorageCellName(ChatColor.WHITE, "storagecontainer", ChatColor.LIGHT_PURPLE, "1M"))
            .setLore(getStorageMaxConfig("1mmax"))
            .setItemMeta()
            .getItem();
        
        this.creativeStorageContainer = new StorageContainer()
            .setCustomModelData(STORAGE_CONTAINER_1M_MODEL_ID)
            .setName(getStorageCellName(ChatColor.WHITE, "storagecontainer", ChatColor.GRAY, ""))
            .setLore(Integer.MAX_VALUE)
            .setItemMeta()
            .getItem();
        
        // Wrenches
        this.storageWrench = createStorageWrench();
        
        this.sorterWrench = createSorterWrench();
        
        // Link Module, Wireless Receiver and Wireless Terminal
        this.linkModule = createLinkModule();
        
        this.receiver = new WirelessReceiver()
            .setName(ChatColor.AQUA + LanguageManager.getValue("receiver"))
            .setItemMeta()
            .getItem();
        
        this.terminal = new WirelessTerminal()
            .setName(ChatColor.AQUA + LanguageManager.getValue("terminal"))
            .setLore(Arrays.asList(
                ChatColor.GRAY + "---------------------",
                ChatColor.RED + ChatColor.BOLD.toString() + LanguageManager.getValue("unlinked"),
                ChatColor.GRAY + LanguageManager.getValue("clicktolink"),
                ChatColor.GRAY + "---------------------",
                ChatColor.AQUA + LanguageManager.getValue("terminal")
            ))
            .setItemMeta()
            .getItem();
        
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
        
        storageContainerIds.add(STORAGE_CONTAINER_1K_MODEL_ID);
        storageContainerIds.add(STORAGE_CONTAINER_4K_MODEL_ID);
        storageContainerIds.add(STORAGE_CONTAINER_16K_MODEL_ID);
        storageContainerIds.add(STORAGE_CONTAINER_64K_MODEL_ID);
        storageContainerIds.add(STORAGE_CONTAINER_256K_MODEL_ID);
        storageContainerIds.add(STORAGE_CONTAINER_1M_MODEL_ID);
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
    
    public static boolean isStorageContainerItem(ItemStack item) {
        if (item == null) {
            return false;
        }
        
        if (item.getType() != Material.IRON_HORSE_ARMOR) {
            return false;
        }
        
        if (item.getItemMeta() == null || !item.hasItemMeta()) {
            return false;
        }
        
        return storageContainerIds.contains(item.getItemMeta().getCustomModelData());
    }
    
    public static boolean isDSPItem(ItemStack item, int modelId) {
        if (item == null) {
            return false;
        }
        
        if (item.getItemMeta() == null || !item.hasItemMeta()) {
            return false;
        }
        
        return item.getItemMeta().getCustomModelData() == modelId;
    }

    public static ItemStack createStorageWrench() {
        return new StorageWrench()
            .setName(ChatColor.AQUA + LanguageManager.getValue("storageloader"))
            .setLore(Arrays.asList(
                ChatColor.GRAY + LanguageManager.getValue("clickempty"),
                ChatColor.GRAY + LanguageManager.getValue("tocreatedsu"),
                "",
                ChatColor.GRAY + LanguageManager.getValue("onetimeuse")
            ))
            .setItemMeta()
            .getItem();
    }

    public static ItemStack createSorterWrench() {
        return new SorterWrench()
            .setName(ChatColor.AQUA + LanguageManager.getValue("sorterloader"))
            .setItemMeta()
            .getItem();
    }

    public static ItemStack createLinkModule() {
        return new LinkModule()
            .setName(ChatColor.AQUA + LanguageManager.getValue("linkmodule"))
            .setLore(Arrays.asList(
                ChatColor.GRAY + "Click DSU to save coordinates",
                ChatColor.GRAY + "to this link module."
            ))
            .setItemMeta()
            .getItem();
    }

    public static ItemStack createSpeedUpgrade() {
        return new SpeedUpgrade()
            .setName(ChatColor.WHITE + ChatColor.BOLD.toString() + LanguageManager.getValue("ioupgrade"))
            .setLore(Collections.singletonList(ChatColor.GRAY + LanguageManager.getValue("clicktoupgrade")))
            .hideEnchantment()
            .setItemMeta()
            .setEnchanted()
            .getItem();
    }

    /**
     * Used to compare two ItemStacks from this mod. Returns true if they are of the same type, even if they have different lore.
     * @return true if the items are similar
     */
    public static boolean compareItem(ItemStack item1, ItemStack item2) {
        if (item1.getItemMeta() == null || !item1.hasItemMeta() || item2.getItemMeta() == null || !item2.hasItemMeta()) {
            return false;
        }

        return item1.getItemMeta().getDisplayName().equals(item2.getItemMeta().getDisplayName())
                && item1.getItemMeta().getCustomModelData() == item2.getItemMeta().getCustomModelData();
    }
    
    private int getStorageMaxConfig(String size) {
        if (main.getConfig().getBoolean("countinstacks")) {
            return main.getConfig().getInt(size) * 1024 * 64;
        }
        return main.getConfig().getInt(size) * 1024;
    }
    
    private String getStorageCellName(ChatColor nameColor, String key, ChatColor typeColor, String type) {
        return nameColor + LanguageManager.getValue(key) + " " + typeColor + ChatColor.BOLD + type;
    }
}
