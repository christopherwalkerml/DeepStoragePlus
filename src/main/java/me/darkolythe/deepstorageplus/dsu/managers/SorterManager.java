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
import java.util.logging.Level;
import java.util.stream.Collectors;

import static org.bukkit.Bukkit.getLogger;

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

    private static ItemStack sorterWall;
    
    /*
    Create a dsu Wall item to fill the dsu Inventory
     */
    public static ItemStack getSorterWall() {
    	if (sorterWall != null)
    		return sorterWall;
        ItemStack border = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta bordermeta = border.getItemMeta();
        bordermeta.setDisplayName(ChatColor.DARK_GRAY + LanguageManager.getValue("sorterwalls"));
        border.setItemMeta(bordermeta);

        return sorterWall = border;
    }

    /*
    Create an Empty Block item to fill the dsu Inventory
     */
    public static ItemStack getEmptyBlock() {
        ItemStack storage = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta storagemeta = storage.getItemMeta();
        storagemeta.setDisplayName(ChatColor.YELLOW + LanguageManager.getValue("emptysorterblock"));
        storage.setItemMeta(storagemeta);

        return storage;
    }


    public static boolean sortItems(Inventory sorterInventory) {
        Location sorterLocation = sorterInventory.getLocation();

        // Get the materials we're trying to sort
        Set<Material> materialsToSort = getMaterialsToSort(sorterInventory);

        Map<Material, Set<Inventory>> containingDSUs;

        // Look for a cached set of linked DSUs
        if (DeepStoragePlus.sorterLocationCache.containsKey(sorterLocation)) {
            Map<Material, Set<Location>> cachedDSULocations = DeepStoragePlus.sorterLocationCache.get(sorterLocation);
            containingDSUs = new HashMap<>();
            for (Material material: cachedDSULocations.keySet()) {
                containingDSUs.put(material, SorterManager.getDSUInventories(cachedDSULocations.get(material)));
            }
            if (moveItems(sorterInventory, containingDSUs)) {

                return true;
            }
        }

        // Get all linked DSUs
        Set<Location> locations = new HashSet(SorterManager.getLinkedLocations(sorterInventory));
        Set<Inventory> dsuInventories = SorterManager.getDSUInventories(locations);

        // Get the DSUs that contain those materials, organized by material
        containingDSUs = getDSUsWithMaterial(dsuInventories, materialsToSort);

        // Update cache
        updateCache(sorterLocation, containingDSUs);

        return moveItems(sorterInventory, containingDSUs);
    }

    private static void updateCache(Location sorterLocation, Map<Material, Set<Inventory>> containingDSUs) {
        if (DeepStoragePlus.sorterLocationCache.containsKey(sorterLocation)) {
            Map<Material, Set<Location>> existingCache = DeepStoragePlus.sorterLocationCache.get(sorterLocation);
            for (Material material: containingDSUs.keySet()) {
                if (existingCache.containsKey(material)) {
                    existingCache.get(material).addAll(containingDSUs.get(material).stream().map(Inventory::getLocation).collect(Collectors.toSet()));
                }
                else {
                    existingCache.put(material, containingDSUs.get(material).stream().map(Inventory::getLocation).collect(Collectors.toSet()));
                }
            }
        } else {
            Map<Material, Set<Location>> cachedDSULocations = new HashMap<>();
            for (Material material: containingDSUs.keySet()) {
                cachedDSULocations.put(material, containingDSUs.get(material).stream().map(Inventory::getLocation).collect(Collectors.toSet()));
            }
            DeepStoragePlus.sorterLocationCache.put(sorterLocation, cachedDSULocations);
        }
    }

    private static boolean moveItems(Inventory sorterInventory, Map<Material, Set<Inventory>> containingDSUs) {
        // For each item and for each DSU that contains
        boolean success = true;
        for (int i = 0; i < 18; i++) {
            ItemStack item = sorterInventory.getItem(i);
            boolean didMoveAll = false;
            if (item != null && item.getType() != Material.AIR && StorageUtils.hasNoMeta(item)) {
                if (containingDSUs.containsKey(item.getType())) {
                    for (Inventory dsu : containingDSUs.get(item.getType())) { // Try to add the item to each DSU until we successfully add all of it.
                        if (DSUManager.addToDSUSilent(item, dsu)) {
                            didMoveAll = true;
                            break;
                        }
                    }
                }
            }
            else {
                didMoveAll = true;
            }
            success = success && didMoveAll;
        }
        return success;
    }

    private static Set<Material> getMaterialsToSort(Inventory sorterInventory) {
        Set<Material> materialsToSort = new HashSet<>();
        for (int i = 0; i < 18; i++) { // Get each unique item type we're trying to sort
            ItemStack item = sorterInventory.getItem(i);
            if (item != null && StorageUtils.hasNoMeta(item)) {
                materialsToSort.add(item.getType());
            }
        }
        return materialsToSort;
    }

    private static Map<Material, Set<Inventory>> getDSUsWithMaterial(Set<Inventory> dsuInventories, Set<Material> materials) {
        // Get the DSUs that contain those materials
        Map<Material, Set<Inventory>> containingDSUs = new HashMap<>();
        for (Material material : materials) {
            if (!containingDSUs.containsKey(material)) {
                containingDSUs.put(material, new HashSet<>());
            }
            containingDSUs.get(material).addAll(SorterManager.getDSUContainingMaterial(dsuInventories, material));
        }
        return containingDSUs;
    }

    /**
     * Recursively find the location of all sorters and DSUs linked to this sorter and linked to sorters linked to this sorter.
     * We should cache this where possible, but we can't know when it's invalidated with certainty because sorters down the tree
     * may be changed without our knowing. Refreshing only when we fail to find a spot for something or a link card is added to the root sorter
     * should be fine.
     * @param inv
     * @return
     */
    private static List<Location> getLinkedLocations(Inventory inv) {
        List<Location> locations = getLinkedLocations(inv, new ArrayList<>());
        for (int i = 0; i < locations.size(); i++) {
            // Check if this location has another sorter
            Block block = locations.get(i).getBlock();
            if (block.getType() == Material.CHEST && StorageUtils.getChestCustomName(block).orElse("").equals(DeepStoragePlus.sortername)) {
                List<Location> newLocations = getLinkedLocations(((Container) block.getState()).getInventory(), locations);
                locations.addAll(newLocations);
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
                String[] loreLocationArr = ChatColor.stripColor(linkModule.getItemMeta().getLore().get(0)).split("\\s+");
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
                getLogger().log(Level.INFO, "Exception parsing link module lore " + linkModule.getItemMeta().getLore().get(0));
            }
        }
        return Optional.empty();
    }

    /**
     * Get each DSU inventory from a list of locations (which may contain DSUs and Sorters
     * @param locations
     * @return
     */
    public static Set<Inventory> getDSUInventories(Set<Location> locations) {
        Set<Inventory> inventories = new HashSet<>();
        for (Location location: locations) {
            Block block = location.getBlock();
            if (block.getType() == Material.CHEST && StorageUtils.getChestCustomName(block).orElse("").equals(DeepStoragePlus.DSUname)) {
                inventories.add(((Container) block.getState()).getInventory());
            }
        }
        return inventories;
    }

    /**
     * Returns an optional inventory that contains the given material.
     */
    public static Set<Inventory> getDSUContainingMaterial(Set<Inventory> inventories, Material material) {
        Set<Inventory> DSUs = new HashSet<>();
        for (Inventory inventory: inventories) {
            if (DSUManager.dsuContainsType(inventory, material)) {
                DSUs.add(inventory);
            }
        }
        return DSUs;
    }
}
