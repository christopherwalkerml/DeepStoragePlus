package me.darkolythe.deepstorageplus;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class StorageBreakListener implements Listener {

    DeepStoragePlus main;
    StorageBreakListener(DeepStoragePlus plugin) {
        main = plugin;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    private void onStorageBreak(BlockBreakEvent event) {
        if (!event.isCancelled()) {
            if (event.getBlock().getState() instanceof Container) {
                Container chest = (Container) event.getBlock().getState();
                if (chest.getInventory().contains(DSUManager.getDSUWall())) {
                    DoubleChest doublechest = (DoubleChest) chest.getInventory().getHolder();
                    event.setCancelled(true);

                    Container chestLeft = (Container)doublechest.getLeftSide();
                    Container chestRight = (Container)doublechest.getRightSide();

                    removeItems(chestLeft);
                    removeItems(chestRight);
                    event.setDropItems(false);
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
        for (int i = 0; i < chest.getInventory().getContents().length; i++) {
            ItemStack item = chest.getInventory().getItem(i);
            if (item != null) {
                if (!(item.hasItemMeta() && item.getType() == Material.STONE_AXE)) {
                    chest.getInventory().setItem(i, null);
                }
            }
        }
    }
}
