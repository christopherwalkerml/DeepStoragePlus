package me.darkolythe.deepstorageplus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

class DSUUpdateManager {

    private DeepStoragePlus main;
    DSUUpdateManager(DeepStoragePlus plugin) {
        main = plugin;
    }

    /*
    Update the items in the DSU. This is done when items are added, taken, Storage Containers are added, taken, and when opening the DSU.
     */
    void updateItems(Inventory inv) {
        addNewItems(inv);
        removeOldItems(inv);
        sortInventory(inv);
    }

    private void sortInventory(Inventory inv) {
        ItemStack IOSettings = inv.getItem(53);
        if (IOSettings != null && IOSettings.hasItemMeta() && IOSettings.getItemMeta().hasLore()) {
            String sort = IOSettings.getItemMeta().getLore().get(2).replace(ChatColor.GRAY + "Sorting By: " + ChatColor.BLUE, "");

            if (sort.equals("container")) {
                clearItems(inv);
                addNewItems(inv);
            } else if (sort.equals("amount")) {
                clearItems(inv);
                Map<Material, Double> data = new HashMap<>();
                List<Material> mats = getMats(inv);
                for (Material m : mats) {
                    data.put(m, (double) DSUManager.getTotalMaterialAmount(inv, m));
                }
                int dataAmount = data.keySet().size();
                for (int i = 0; i < dataAmount; i++) {
                    double top = 0;
                    Material topMat = Material.AIR;
                    for (Material m : data.keySet()) {
                        if ((data.get(m) > top)) {
                            topMat = m;
                            top = data.get(m);
                        }
                    }
                    inv.addItem(new ItemStack(topMat));
                    data.remove(topMat);
                }
            } else if (sort.equalsIgnoreCase("alpha")) {
                List<Material> mats = getMats(inv);
                Collections.sort(mats);
                clearItems(inv);
                for (Material m : mats) {
                    inv.addItem(new ItemStack(m));
                }
            } else if (sort.equalsIgnoreCase("ID")) {
                clearItems(inv);
                Map<Material, Double> data = new HashMap<>();
                List<Material> mats = getMats(inv);
                for (Material m : mats) {
                    data.put(m, IDLibrary.getID(m));
                }
                int dataAmount = data.keySet().size();
                for (int i = 0; i < dataAmount; i++) {
                    double bottom = 1000000;
                    Material topMat = Material.AIR;
                    for (Material m : data.keySet()) {
                        if ((data.get(m) < bottom)) {
                            topMat = m;
                            bottom = data.get(m);
                        }
                    }
                    inv.addItem(new ItemStack(topMat));
                    data.remove(topMat);
                }
            }
        }
        updateInventory(inv);
    }

    private static void addNewItems(Inventory inv) {
        for (int i = 0; i < 5; i++) {
            ItemStack container = inv.getItem(8 + (9 * i));
            if (container.hasItemMeta() && container.getItemMeta().hasDisplayName() && container.getItemMeta().getDisplayName().contains("Storage Container")) {
                List<Material> mats = DSUManager.getTypes(container.getItemMeta().getLore());
                for (Material m : mats) {
                    ItemStack item = new ItemStack(m);
                    boolean canAdd = true;
                    for (ItemStack it : inv.getContents()) { //This section adds the items based on the Storage Containers
                        if (item.equals(it)) {
                            canAdd = false;
                            break;
                        }
                    }
                    if (canAdd) {
                        inv.addItem(item);
                    }
                }
            }
        }
    }

    private static void removeOldItems(Inventory inv) {
        for (int x = 0; x < 53; x++) {
            boolean isHere = false;
            if (x % 9 != 8 && x % 9 != 7) {
                if (inv.getItem(x) != null && inv.getItem(x).getType() != Material.AIR) {
                    for (int i = 0; i < 5; i++) {
                        ItemStack container = inv.getItem(8 + (9 * i));
                        if (container.hasItemMeta() && container.getItemMeta().hasDisplayName() && container.getItemMeta().getDisplayName().contains("Storage Container")) {
                            List<Material> mats = DSUManager.getTypes(container.getItemMeta().getLore());
                            for (Material m : mats) {
                                ItemStack it = new ItemStack(m);
                                if (it.equals(inv.getItem(x))) { //This section removes items that should no longer be there (whether it be they were removed, or their container was)
                                    isHere = true;
                                    break;
                                }
                            }
                        }
                        if (isHere) {
                            break;
                        }
                    }
                    if (!isHere) {
                        inv.getItem(x).setAmount(0);
                    }
                }
            }
        }
    }

    private void updateInventory(Inventory inv) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
            @Override
            public void run() {
                inv.getLocation().getBlock().getState().update();
            }
        }, 1);
    }

    private static void clearItems(Inventory inv) {
        for (int i = 0; i < 54; i++) {
            if (i % 9 != 8 && i % 9 != 7) {
                inv.setItem(i, null);
            }
        }
    }

    private static List<Material> getMats(Inventory inv) {
        Set<Material> mats = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            ItemStack container = inv.getItem(8 + (9 * i));
            if (container != null && container.getType() != Material.WHITE_STAINED_GLASS_PANE) {
                List<Material> tempMats = DSUManager.getTypes(container.getItemMeta().getLore());
                mats.addAll(tempMats);
            }
        }
        return new ArrayList<>(mats);
    }
}
