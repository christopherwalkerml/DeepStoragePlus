package me.darkolythe.deepstorageplus;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class InventoryListener implements Listener {

    private DeepStoragePlus main;
    public InventoryListener(DeepStoragePlus plugin) {
        this.main = plugin; // set it equal to an instance of main
    }

    @EventHandler
    private void onStorageOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if (event.getView().getTitle().equals(DeepStoragePlus.DSUname)) {
                verifyInventory(event.getInventory());
            }
        }
    }

    @EventHandler
    private void onStorageInteract(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();
                if (event.getView().getTitle().equals(DeepStoragePlus.DSUname)) {
                    if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()) {
                        if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.DARK_GRAY + "DSU Walls")) { //DSU WALLS
                            event.setCancelled(true);
                        } else if (event.getSlot() % 9 == 8 && event.getSlot() != 53) { //RIGHT BAR FOR STORAGE CELLS
                            if (event.getCurrentItem().getType() == Material.WHITE_STAINED_GLASS_PANE) {
                                event.setCancelled(true);
                                if (event.getCursor() != null && event.getCursor().hasItemMeta()) {
                                    if (event.getCursor().getItemMeta().getDisplayName().contains("Storage Container") && event.getCursor().getItemMeta().isUnbreakable()) {
                                        event.getInventory().setItem(event.getSlot(), event.getCursor());
                                        event.getCursor().setAmount(0);
                                    }
                                }
                            } else if (event.getCursor() != null && event.getCursor().getType() != Material.AIR) {
                                if (!(event.getCursor().hasItemMeta() && event.getCursor().getItemMeta().getDisplayName().contains("Storage Container") && event.getCursor().getItemMeta().isUnbreakable())) {
                                    event.setCancelled(true);
                                }
                            } else if (event.getCursor() == null || event.getCursor().getType() == Material.AIR) {
                                event.setCancelled(true);
                                player.setItemOnCursor(event.getCurrentItem().clone());
                                event.getInventory().setItem(event.getSlot(), getEmptyCell());
                            }
                        } else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GRAY + "DSU IO Configuration")) { //BOTTOM RIGHT FOR SETTINGS
                            event.setCancelled(true);
                        }
                    }
                    //note: keep track of number key press
                    //if shift click in inv
                    //if double shift in inv
                    if (event.getClickedInventory() == player.getInventory()) {
                        if (event.isShiftClick()) {
                            System.out.println("yeah its shift");
                        }
                    } else { //if click in DSU with item
                        if (event.getCursor() != null && event.getCursor().getType() != Material.AIR) {
                            System.out.println("storage " + countStorage(event.getInventory(), "Current Storage: "));
                            System.out.println("types " + countStorage(event.getInventory(), "Current Types: "));
                        }
                    }
                }
            }
        }
    }

    private static void verifyInventory(Inventory inv) {
        ItemStack border = getDSUWall();
        for (int i = 0; i < 6; i++) {
            inv.setItem(7 + (9 * i), border.clone());
        }

        ItemStack storage = getEmptyCell();
        for (int i = 0; i < 5; i++) {
            if (inv.getItem(8 + (9 * i)) == null) {
                inv.setItem(8 + (9 * i), storage.clone());
            }
        }

        ItemStack settings = new ItemStack(Material.STONE_SHOVEL);
        ItemMeta settingsmeta = settings.getItemMeta();
        settingsmeta.setDisplayName(ChatColor.GRAY + "DSU IO Configuration");
        settingsmeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        settingsmeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        settingsmeta.setUnbreakable(true);
        settings.setItemMeta(settingsmeta);
        settings.setDurability((short) 130);
        inv.setItem(53, settings);
    }

    private static ItemStack getDSUWall() {
        ItemStack border = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta bordermeta = border.getItemMeta();
        bordermeta.setDisplayName(ChatColor.DARK_GRAY + "DSU Walls");
        border.setItemMeta(bordermeta);

        return border;
    }

    private static ItemStack getEmptyCell() {
        ItemStack storage = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta storagemeta = storage.getItemMeta();
        storagemeta.setDisplayName(ChatColor.YELLOW + "Empty Storage Cell");
        storage.setItemMeta(storagemeta);

        return storage;
    }

    private int countStorage(Inventory inv, String typeString) {
        int spaceTotal = 0;
        int spaceCur = 0;
        for (int i = 0; i < 5; i++) {
            ItemStack container = inv.getItem(8 + (9 * i));
            if (container != null && container.hasItemMeta() && container.getItemMeta().hasLore()) {
                ItemMeta meta = container.getItemMeta();
                List<String> lore = meta.getLore();
                for (int x = 0; x < lore.size(); x++) {
                    if (lore.get(x).contains(typeString)) {
                        String data = getData(lore.get(x));
                        spaceCur += getCurrentData(data);
                        spaceTotal += getMaxData(data);
                    }
                }
            }
        }
        return spaceTotal - spaceCur;
    }

    private static String getData(String lore) {
        int spaceAt = -1;
        String data = "";
        for (int z = 0; z < lore.length(); z++) {
            if (spaceAt != -1) {
                data += lore.charAt(z);
            }
            if (lore.charAt(z) == ' ' && z > 15) {
                spaceAt = z;
            }
        }
        return data;
    }

    private static int getCurrentData(String data) {
        String curData = "";
        int slashAt = -1;
        for (int z = 0; z < data.length(); z++) {
            if (data.charAt(z) == '/') {
                slashAt = z;
            }
            if (slashAt == -1) {
                curData += data.charAt(z);
            }
        }
        return Integer.parseInt(curData);
    }

    private static int getMaxData(String data) {
        String maxData = "";
        int slashAt = -1;
        for (int z = 0; z < data.length(); z++) {
            if (slashAt != -1) {
                maxData += data.charAt(z);
            }
            if (data.charAt(z) == '/') {
                slashAt = z;
            }
        }
        return Integer.parseInt(maxData);
    }
}
