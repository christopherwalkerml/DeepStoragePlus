package me.darkolythe.deepstorageplus;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryListener implements Listener {

    private DeepStoragePlus main;
    public InventoryListener(DeepStoragePlus plugin) {
        this.main = plugin; // set it equal to an instance of main
    }

    @EventHandler
    private void onStorageOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if (event.getView().getTitle().equals(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Deep Storage Unit")) {
                verifyInventory(event.getInventory());
            }
        }
    }

    @EventHandler
    private void onStorageInteract(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();
                if (event.getView().getTitle().equals(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Deep Storage Unit")) {
                    if (event.getCurrentItem() != null) {
                        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.DARK_GRAY + "DSU Walls")) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    private void verifyInventory(Inventory inv) {
        ItemStack border = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta bordermeta = border.getItemMeta();
        bordermeta.setDisplayName(ChatColor.DARK_GRAY + "DSU Walls");
        border.setItemMeta(bordermeta);
        for (int i = 0; i < 6; i++) {
            inv.setItem(7 + (9 * i), border);
        }

        ItemStack storage = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta storagemeta = storage.getItemMeta();
        storagemeta.setDisplayName(ChatColor.YELLOW + "Empty Storage Cell");
        storage.setItemMeta(storagemeta);
        for (int i = 0; i < 5; i++) {
            if (inv.getItem(8 + (9 * i)) == null) {
                inv.setItem(8 + (9 * i), storage);
            }
        }

        ItemStack settings = new ItemStack(Material.STONE_SHOVEL);
        ItemMeta settingsmeta = settings.getItemMeta();
        settingsmeta.setDisplayName(ChatColor.GRAY + "DSU IO Configuration");
        settings.setItemMeta(settingsmeta);
        settings.setDurability((short) 130);
    }

    private int getStorageSpace(Inventory inv) {
        int spaceTotal = 0;
        int spaceCur = 0;
        for (int i = 0; i < 5; i++) {
            ItemStack container = inv.getItem(8 + (9 * i));
            if (container != null) {
                ItemMeta meta = container.getItemMeta();
                System.out.println(meta.getLore());
            }
        }
        return spaceTotal - spaceCur;
    }

    private int getTypeSpace(Inventory inv) {

        return 0;
    }
}
