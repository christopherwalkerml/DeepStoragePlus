package me.darkolythe.deepstorageplus.dsu.managers;

import me.darkolythe.deepstorageplus.DeepStoragePlus;
import me.darkolythe.deepstorageplus.dsu.StorageUtils;
import me.darkolythe.deepstorageplus.utils.ItemList;
import me.darkolythe.deepstorageplus.utils.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class SorterManager {

    DeepStoragePlus main;
    public SorterManager(DeepStoragePlus plugin) {
        main = plugin;
    }

    /*
    Create the sorter inventory and make it so that it's correct upon opening
    */
    public static void verifyInventory(Inventory inv, Player player) {
        for (int i = 0; i < 9; i++) {
            inv.setItem( 18 + i, getSorterWall());
        }

        for (int i = 0; i < 27; i++) {
            if (inv.getItem(27 + i) == null) {
                inv.setItem(27 + i, getEmptyBlock());
            }
        }
    }

    /*
    Create a dsu Wall item to fill the dsu Inventory
     */
    public static ItemStack getSorterWall() {
        ItemStack border = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta bordermeta = border.getItemMeta();
        bordermeta.setDisplayName(ChatColor.DARK_GRAY + LanguageManager.getValue("sorterwalls"));
        border.setItemMeta(bordermeta);

        return border;
    }

    /*
    Create an Empty Block item to fill the dsu Inventory
     */
    public static ItemStack getEmptyBlock() {
        ItemStack storage = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta storagemeta = storage.getItemMeta();
        storagemeta.setDisplayName(ChatColor.YELLOW + LanguageManager.getValue("emptystorageblock"));
        storage.setItemMeta(storagemeta);

        return storage;
    }


    public static void sortItems(Inventory sorterInventory) {
        // Get linked DSUs
        List<Location> locations = SorterManager.getLinkedLocations(sorterInventory);
        List<Inventory> dsuInventories = SorterManager.getDSUInventories(locations);

        for (int i = 0; i < 18; i++) { // For each input slot of this sorter
            ItemStack item = sorterInventory.getItem(i);
            if (item != null && StorageUtils.hasNoMeta(item)) {
                List<Inventory> containingDSUs = SorterManager.getDSUContainingMaterial(dsuInventories, item.getType());
                for (Inventory dsu : containingDSUs) { // Try to add the item to each DSU until we successfully add all of it.
                    if (DSUManager.addToDSUSilent(item, dsu)) {
                        break;
                    }
                }
            }
        }
    }

    /**
     * Recursively find the location of all sorters and DSUs linked to this sorter and linked to sorters linked to this sorter.
     * We should cache this where possible, but we can't know when it's invalidated with certainty because sorters down the tree
     * may be changed without our knowing. Refreshing only when we fail to find a spot for something or a link card is added to the root sorter
     * should be fine.
     * @param inv
     * @return
     */
    public static List<Location> getLinkedLocations(Inventory inv) {
        List<Location> locations = getLinkedLocations(inv, new ArrayList<>());

        for (int i = 0; i < locations.size(); i++) {
            // Check if this location has another sorter
            Block block = locations.get(i).getBlock();
            if (block.getType() == Material.CHEST && ((Container) block.getState()).getCustomName() != null && ((Container) block.getState()).getCustomName().equals(DeepStoragePlus.sortername)) {
                locations.addAll(getLinkedLocations(((Container) block.getState()).getInventory(), locations));
            }
        }
        return locations;
    }

    private static List<Location> getLinkedLocations(Inventory inv, List<Location> locations) {
        // Check each link module slot for locations referenced in this sorter
        List<Location> newLocations = new ArrayList<>();
        for (int i = 27; i < 54; i++) {
            ItemStack item = inv.getItem(i);
            if (item != null) { // Only link modules should be possible in these slots
                getLinkModuleLocation(item).ifPresent(x -> {if (!locations.contains(x)) newLocations.add(x);});
            }
        }
        return newLocations;
    }

    private static Optional<Location> getLinkModuleLocation(ItemStack linkModule) {
        if (linkModule.hasItemMeta()
                && linkModule.getItemMeta().hasDisplayName() && linkModule.getItemMeta().getDisplayName().equals(ItemList.createLinkModule().getItemMeta().getDisplayName())
                && linkModule.getItemMeta().hasLore() && linkModule.getItemMeta().getLore().size() > 0) {
            try {
                String[] loreLocationArr = linkModule.getItemMeta().getLore().get(0).split("\\s+");
                if (loreLocationArr.length == 4) {
                    return Optional.of(new Location(
                            Bukkit.getWorld(loreLocationArr[0]),
                            Double.parseDouble(loreLocationArr[1]),
                            Double.parseDouble(loreLocationArr[2]),
                            Double.parseDouble(loreLocationArr[3]))
                    );
                }
            }
            catch (Exception ignored) {
                System.out.println("Exception parsing link module lore " + linkModule.getItemMeta().getLore().get(0));
            }
        }
        return Optional.empty();
    }

    /**
     * Get each DSU inventory from a list of locations (which may contain DSUs and Sorters
     * @param locations
     * @return
     */
    public static List<Inventory> getDSUInventories(List<Location> locations) {
        List<Inventory> inventories = new ArrayList<>();
        for (Location location: locations) {
            Block block = location.getBlock();
            if (block.getType() == Material.CHEST && ((Container) block.getState()).getCustomName() != null && ((Container) block.getState()).getCustomName().equals(DeepStoragePlus.DSUname)) {
                inventories.add(((Container) block.getState()).getInventory());
            }
        }
        return inventories;
    }

    /**
     * Returns an optional inventory that contains the given material.
     */
    public static List<Inventory> getDSUContainingMaterial(List<Inventory> inventories, Material material) {
        List<Inventory> DSUs = new ArrayList<>();
        for (Inventory inventory: inventories) {
            if (DSUManager.dsuContainsType(inventory, material)) {
                DSUs.add(inventory);
            }
        }
        return DSUs;
    }
}
