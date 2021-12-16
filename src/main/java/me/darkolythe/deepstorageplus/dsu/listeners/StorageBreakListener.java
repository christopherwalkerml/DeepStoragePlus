package me.darkolythe.deepstorageplus.dsu.listeners;

import me.darkolythe.deepstorageplus.DeepStoragePlus;
import me.darkolythe.deepstorageplus.dsu.StorageUtils;
import me.darkolythe.deepstorageplus.dsu.managers.DSUManager;
import me.darkolythe.deepstorageplus.dsu.managers.SorterManager;
import me.darkolythe.deepstorageplus.utils.ItemList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import static me.darkolythe.deepstorageplus.dsu.managers.SettingsManager.getLocked;
import static me.darkolythe.deepstorageplus.dsu.managers.SettingsManager.getLockedUsers;

public class StorageBreakListener implements Listener {

    DeepStoragePlus main;
    public StorageBreakListener(DeepStoragePlus plugin) {
        main = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    private void onStorageBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!event.isCancelled()) {
            if (event.getBlock().getState() instanceof Container) {
                Container chest = (Container) event.getBlock().getState();
                if (chest.getInventory().contains(DSUManager.getDSUWall()) || chest.getInventory().contains(SorterManager.getSorterWall())) {

                    ItemStack lock = chest.getInventory().getItem(53);
                    boolean isOp = player.hasPermission("deepstorageplus.adminopen");
                    boolean canOpen = getLocked(lock, player);
                    if (canOpen || isOp || getLockedUsers(lock).size() == 0) {

                        DoubleChest doublechest = (DoubleChest) chest.getInventory().getHolder();
                        event.setCancelled(true);

                        Container chestLeft = (Container) doublechest.getLeftSide();
                        Container chestRight = (Container) doublechest.getRightSide();

                        removeItems(chestLeft);
                        removeItems(chestRight);
                        event.setDropItems(false);
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    private void breakStorage(Container chest) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
            @Override
            public void run() {
                chest.getWorld().getBlockAt(chest.getLocation()).setType(Material.AIR);
            }
        }, 1);
        chest.getWorld().dropItemNaturally(chest.getLocation(), new ItemStack(Material.CHEST, 1));
    }

    private void removeItems(Container chest) {
        chest.setCustomName(null);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
            @Override
            public void run() {
                emptyChest(chest);
            }
        }, 1);
        breakStorage(chest);
    }

    private static void emptyChest(Container chest) {
        if (StorageUtils.isSorter(chest.getInventory())) {
            chest.getInventory().addItem(ItemList.createSorterWrench());
        } else if (StorageUtils.isDSU(chest.getInventory())) {
            chest.getInventory().addItem(ItemList.createStorageWrench());
        }
        for (int i = 0; i < chest.getInventory().getContents().length; i++) {
            ItemStack item = chest.getInventory().getItem(i);
            if (item != null) {
                if (!(item.hasItemMeta() && (item.getType() == Material.STONE_AXE || item.getType() == Material.STONE_SHOVEL))) {
                    chest.getInventory().setItem(i, null);
                } else {
                    chest.getWorld().dropItemNaturally(chest.getLocation(), item);
                    chest.getInventory().setItem(i, null);
                }
            }
        }
    }
}
