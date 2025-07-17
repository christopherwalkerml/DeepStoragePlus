package me.darkolythe.deepstorageplus.dsu.managers;

import me.darkolythe.deepstorageplus.DeepStoragePlus;
import me.darkolythe.deepstorageplus.utils.LanguageManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class WirelessManager {

    public static ItemStack createTerminal() {
        ItemStack terminal = new ItemStack(Material.STONE_AXE);
        ItemMeta meta = terminal.getItemMeta();
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setDisplayName(ChatColor.AQUA + LanguageManager.getValue("terminal"));
        meta.setLore(Arrays.asList(ChatColor.GRAY + "---------------------",
                                   ChatColor.RED.toString() + ChatColor.BOLD.toString() + LanguageManager.getValue("unlinked"),
                                   ChatColor.GRAY.toString() + LanguageManager.getValue("clicktolink"),
                                   ChatColor.GRAY + "---------------------",
                                   ChatColor.AQUA.toString() + LanguageManager.getValue("terminal")));
        // set texture data ID
        meta.setCustomModelData(13000);

        terminal.setItemMeta(meta);
        return terminal;
    }

    public static ItemStack createReceiver() {
        ItemStack receiver = new ItemStack(Material.STONE_AXE);
        ItemMeta meta = receiver.getItemMeta();
        meta.setUnbreakable(true);
        meta.setDisplayName(ChatColor.AQUA + LanguageManager.getValue("receiver"));
        // set texture data ID
        meta.setCustomModelData(13001);
        receiver.setItemMeta(meta);
        return receiver;
    }

    public static void updateTerminal(ItemStack terminal, int x, int y, int z, World world) {
        ItemMeta meta = terminal.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        String range = DeepStoragePlus.maxrange == -1 ? "none" : String.valueOf(DeepStoragePlus.maxrange);

        meta.setLore(Arrays.asList(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + LanguageManager.getValue("linked"),
                                   ChatColor.GRAY.toString() + "X: " + ChatColor.RED.toString() + x,
                                   ChatColor.GRAY.toString() + "Y: " + ChatColor.RED.toString() + y,
                                   ChatColor.GRAY.toString() + "Z: " + ChatColor.RED.toString() + z,
                                   ChatColor.GRAY.toString() + LanguageManager.getValue("world") + ": " + ChatColor.RED.toString() + world.getName(),
                                   ChatColor.GRAY.toString() + LanguageManager.getValue("maxdistance") + ": " + ChatColor.RED.toString() + range,
                                   "",
                                   ChatColor.GRAY + LanguageManager.getValue("shiftswap"),
                                   ChatColor.AQUA.toString() + LanguageManager.getValue("terminal")));
        terminal.setItemMeta(meta);
        terminal.addUnsafeEnchantment(Enchantment.UNBREAKING, 1);
    }

    public static Inventory getWirelessDSU(ItemStack terminal, Player player) {
        ItemMeta meta = terminal.getItemMeta();
        List<String> lore = meta.getLore();
        int x = Integer.parseInt(lore.get(1).replaceAll("^[^_]*:", "").replace(" ", "").replace(ChatColor.RED.toString(), ""));
        int y = Integer.parseInt(lore.get(2).replaceAll("^[^_]*:", "").replace(" ", "").replace(ChatColor.RED.toString(), ""));
        int z = Integer.parseInt(lore.get(3).replaceAll("^[^_]*:", "").replace(" ", "").replace(ChatColor.RED.toString(), ""));
        int range = -1;
        if (!lore.get(5).contains("none") && lore.get(5).length() > 1) {
            range = Integer.parseInt(lore.get(5).replaceAll("^[^_]*:", "").replace(" ", "").replace(ChatColor.RED.toString(), ""));
        }

        String world = ChatColor.GRAY.toString() + LanguageManager.getValue("world") + ": " + player.getWorld().getName();
        String newWorld = ChatColor.GRAY.toString() + LanguageManager.getValue("world") + ": " + ChatColor.RED.toString() + player.getWorld().getName();
        if (!world.equals(lore.get(4)) && !newWorld.equals(lore.get(4))) {
            player.sendMessage(DeepStoragePlus.prefix + ChatColor.RED + LanguageManager.getValue("cantopenin") + " " + ChatColor.GRAY + player.getWorld().getName());
            return null;
        }

        Block block = player.getWorld().getBlockAt(x, y, z);
        if (block.getType() != Material.CHEST) {
            player.sendMessage(DeepStoragePlus.prefix + ChatColor.RED + LanguageManager.getValue("dsunolongerthere"));
            return null;
        }

        if (block.getLocation().distance(player.getLocation()) > range && range != -1) {
            player.sendMessage(DeepStoragePlus.prefix + ChatColor.RED + LanguageManager.getValue("toofar"));
            return null;
        }

        Chest chest = (Chest) block.getState();
        if (chest.getInventory().contains(DSUManager.getDSUWall())) {
            return chest.getInventory();
        } else {
            player.sendMessage(DeepStoragePlus.prefix + ChatColor.RED + LanguageManager.getValue("dsunolongerthere"));
        }

        return null;
    }

    public static boolean isWirelessTerminal(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName() && meta.hasLore()) {
                if (meta.getLore().get(meta.getLore().size() - 1).equals(ChatColor.AQUA + LanguageManager.getValue("terminal"))) {
                    return true;
                }
            }
        }
        return false;
    }
}
