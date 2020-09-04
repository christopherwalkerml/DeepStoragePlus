package me.darkolythe.deepstorageplus.dsu.managers;

import me.darkolythe.deepstorageplus.utils.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.darkolythe.deepstorageplus.dsu.StorageUtils.stringToMat;

public class SettingsManager {

    /*
    Takes the dsu's inventory and lets the player choose an item to export from the list. (Also allow for other things? we shall see. maybe amount of item? Upgrades?)
     */
    public static Inventory createIOInventory(Inventory DSUInv) {
        Inventory IOInv = Bukkit.createInventory(null, 54, ChatColor.BLUE.toString() + ChatColor.BOLD.toString() + LanguageManager.getValue("dsuioconfig"));

        for (int x = 0; x < 53; x++) {
            if (x % 9 != 8) {
                if (DSUInv.getItem(x) != null) {
                    IOInv.setItem(x, DSUInv.getItem(x).clone());
                }
            }
        }

        ItemStack IOItem = DSUInv.getItem(53);
        ItemMeta IOMeta = IOItem.getItemMeta();
        List<String> lore = IOMeta.getLore();

        /*
         * Create the IO settings item with use of the DSUinv to check for existing IO items
         */
        if (lore.get(0).contains(ChatColor.BLUE + LanguageManager.getValue("all"))) {
            IOInv.setItem(8, getEmptyInputSlot());
        } else {
            ItemStack newInput = getEmptyInputSlot();
            newInput.setType(stringToMat(lore.get(0), ChatColor.GRAY + LanguageManager.getValue("input") + ": " + ChatColor.GREEN));
            ItemMeta inputMeta = newInput.getItemMeta();
            inputMeta.setDisplayName(ChatColor.GRAY + LanguageManager.getValue("input") + ": " + lore.get(0).replace(ChatColor.GRAY + LanguageManager.getValue("input") + ": ", ""));
            inputMeta.setLore(Arrays.asList(ChatColor.GRAY + LanguageManager.getValue("clicktoclear")));
            newInput.setItemMeta(inputMeta);
            IOInv.setItem(8, newInput);
        }
        if (lore.get(1).contains(ChatColor.BLUE + LanguageManager.getValue("none"))) {
            IOInv.setItem(17, getEmptyOutputSlot());
        } else {
            ItemStack newOutput = getEmptyOutputSlot();
            newOutput.setType(stringToMat(lore.get(1), ChatColor.GRAY + LanguageManager.getValue("output") + ": " + ChatColor.GREEN));
            ItemMeta outputMeta = newOutput.getItemMeta();
            outputMeta.setDisplayName(ChatColor.GRAY + LanguageManager.getValue("output") + ": " + lore.get(1).replace(ChatColor.GRAY + LanguageManager.getValue("output") + ": ", ""));
            outputMeta.setLore(Arrays.asList(ChatColor.GRAY + LanguageManager.getValue("clicktoclear")));
            newOutput.setItemMeta(outputMeta);
            IOInv.setItem(17, newOutput);
        }

        ItemStack sortSlot = new ItemStack(Material.COMPASS);
        ItemMeta sortMeta = sortSlot.getItemMeta();
        sortMeta.setDisplayName(ChatColor.GRAY + LanguageManager.getValue("sortingby") + ": " + lore.get(2).replace(ChatColor.GRAY + LanguageManager.getValue("sortingby") + ": ", ""));
        sortMeta.setLore(Arrays.asList(ChatColor.GRAY + LanguageManager.getValue("changesorting"),
                ChatColor.BLUE + LanguageManager.getValue("container") + ": " + ChatColor.GRAY + LanguageManager.getValue("sortscontainer"),
                ChatColor.BLUE + LanguageManager.getValue("alpha") + ": " + ChatColor.GRAY + LanguageManager.getValue("sortsalpha"),
                ChatColor.BLUE + LanguageManager.getValue("amount") + ": " + ChatColor.GRAY + LanguageManager.getValue("sortsamount"),
                ChatColor.BLUE + "ID: " + ChatColor.GRAY + LanguageManager.getValue("sortsid")));
        sortSlot.setItemMeta(sortMeta);
        IOInv.setItem(26, sortSlot);

        IOInv.setItem(53, createDSULock(DSUInv));

        return IOInv;
    }

    /*
     * Add lock to IO settings menu
     */
    public static ItemStack createDSULock(Inventory LockInv) {
        ItemStack lock = new ItemStack(Material.TRIPWIRE_HOOK);
        ItemMeta lockmeta = lock.getItemMeta();
        lockmeta.setDisplayName(ChatColor.BLUE + "Lock DSU");
        List<String> locklore = new ArrayList<>();
        locklore.add(ChatColor.GRAY + LanguageManager.getValue("leftclicktoadd"));
        locklore.add(ChatColor.GRAY + LanguageManager.getValue("rightclicktoremove"));
        locklore.add("");
        locklore.add(ChatColor.GRAY + LanguageManager.getValue("owner") + ": " + ChatColor.BLUE + getOwner(LockInv.getItem(53)));

        if (isLocked(LockInv.getItem(53))) {
            locklore.add(ChatColor.RED + LanguageManager.getValue("locked"));
            for (String s : getLockedUsers(LockInv.getItem(53))) {
                locklore.add(ChatColor.WHITE + s);
            }
        } else {
            locklore.add(ChatColor.GREEN + LanguageManager.getValue("unlocked"));
        }
        lockmeta.setLore(locklore);
        lock.setItemMeta(lockmeta);
        return lock;
    }

    /*
    Create an empty input slot item
     */
    private static ItemStack getEmptyInputSlot() {
        ItemStack inputSlot = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta inputMeta = inputSlot.getItemMeta();
        inputMeta.setDisplayName(ChatColor.GRAY + LanguageManager.getValue("input") + ": " + ChatColor.BLUE + LanguageManager.getValue("all"));
        inputMeta.setLore(Arrays.asList(ChatColor.GRAY + LanguageManager.getValue("clicktostart"),
                ChatColor.GRAY + LanguageManager.getValue("clickinput"),
                ChatColor.GRAY + LanguageManager.getValue("leaveasall")));
        inputSlot.setItemMeta(inputMeta);

        return inputSlot;
    }

    /*
    Create an empty output slot item
     */
    private static ItemStack getEmptyOutputSlot() {
        ItemStack outputSlot = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
        ItemMeta outputMeta = outputSlot.getItemMeta();
        outputMeta.setDisplayName(ChatColor.GRAY + LanguageManager.getValue("output") + ": " + ChatColor.BLUE + LanguageManager.getValue("none"));
        outputMeta.setLore(Arrays.asList(ChatColor.GRAY + LanguageManager.getValue("clicktostart"),
                ChatColor.GRAY + LanguageManager.getValue("clickoutput")));
        outputSlot.setItemMeta(outputMeta);

        return outputSlot;
    }

    /*
    Initialize the selection tool by cancelling all other current selections and enchanting the current slot
     */
    public static void startSelection(int slot, Inventory inv) {
        for (int i = 0; i < inv.getContents().length; i++) {
            if (inv.getItem(i) != null) {
                if (inv.getItem(i).getEnchantments().size() > 0) {
                    ItemStack newItem = inv.getItem(i);
                    newItem.removeEnchantment(Enchantment.DURABILITY);
                    inv.setItem(i, newItem);
                }
            }
        }
        ItemStack item;
        if (slot == 8) {
            item = getEmptyInputSlot();
        } else {
            item = getEmptyOutputSlot();
        }
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        inv.setItem(slot, item);
    }

    public static String getOwner(ItemStack IOSettings) {
        if (IOSettings != null) {
            ItemMeta meta = IOSettings.getItemMeta();
            if (meta != null) {
                List<String> lore = meta.getLore();
                if (lore != null) {
                    for (String s : lore) {
                        String ownstr = ChatColor.GRAY + LanguageManager.getValue("owner");
                        if (s.contains(ownstr + ": ")) {
                            return s.replaceAll(ownstr + ": " + ChatColor.BLUE, "");
                        }
                    }
                }
            }
        }
        return "";
    }

    public static List<String> getLockedUsers(ItemStack IOSettings) {
        List<String> users = new ArrayList<>();
        if (IOSettings != null) {
            ItemMeta meta = IOSettings.getItemMeta();
            if (meta != null) {
                List<String> lore = meta.getLore();
                boolean pastLocked = false;
                if (lore != null) {
                    for (String s : lore) {
                        if (pastLocked) {
                            if (s.contains(ChatColor.WHITE.toString())) {
                                users.add(s.replaceAll(ChatColor.WHITE.toString(), ""));
                            } else {
                                break;
                            }
                        }
                        if (s.equals(ChatColor.RED + LanguageManager.getValue("locked"))) {
                            pastLocked = true;
                        }
                    }
                }
            }
        }
        return users;
    }

    public static boolean isLocked(ItemStack IOSettings) {
        if (IOSettings != null) {
            ItemMeta meta = IOSettings.getItemMeta();
            if (meta != null) {
                List<String> lore = meta.getLore();
                if (lore != null) {
                    for (String s : lore) {
                        if (s.equals(ChatColor.RED + LanguageManager.getValue("locked"))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean getLocked(ItemStack IOSettings, Player player) {
        List<String> users = getLockedUsers(IOSettings);
        users.add(getOwner(IOSettings));
        for (String s : users) {
            if (player.getName().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static int getSpeedUpgrade(ItemStack item) {
        for (String lore : item.getItemMeta().getLore()) {
            if (lore.contains(ChatColor.GRAY + LanguageManager.getValue("iospeed") + ": ")) {
                return Integer.parseInt(ChatColor.stripColor(lore).replaceAll("[\\D]", ""));
            }
        }
        return 0;
    }

    public static ItemStack addSpeedUpgrade(ItemStack item) {
        int i = 0;
        int amt = 0;
        boolean found = false;

        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();

        for (String l : lore) {
            if (l.contains(ChatColor.GRAY + LanguageManager.getValue("iospeed") + ": ")) {
                found = true;
                amt = Integer.parseInt(ChatColor.stripColor(l).replaceAll("[\\D]", ""));
                if (amt >= 63) {
                    return null;
                }
                break;
            }
            i++;
        }

        if (found) {
            lore.set(i, ChatColor.GRAY + LanguageManager.getValue("iospeed") + ": " + ChatColor.GREEN + "+" + (amt + 1));
        } else {
            lore.add(ChatColor.GRAY + LanguageManager.getValue("iospeed") + ": " + ChatColor.GREEN + "+" + (amt + 1));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
