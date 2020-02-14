package me.darkolythe.deepstorageplus.dsu.managers;

import me.darkolythe.deepstorageplus.utils.LanguageManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class WirelessManager {

    public static ItemStack createTerminal() {
        ItemStack receiver = new ItemStack(Material.STONE_SHOVEL);
        ItemMeta meta = receiver.getItemMeta();
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setDisplayName(ChatColor.AQUA + LanguageManager.getValue("terminal"));
        meta.setLore(Arrays.asList(ChatColor.GRAY + "---------------------",
                                   ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + LanguageManager.getValue("unlinked"),
                                   ChatColor.GRAY.toString() + LanguageManager.getValue("clicktolink"),
                                   ChatColor.GRAY + "---------------------"));
        receiver.setItemMeta(meta);
        receiver.setDurability((short)124);
        return receiver;
    }

    public static ItemStack createReceiver() {
        ItemStack receiver = new ItemStack(Material.STONE_SHOVEL);
        ItemMeta meta = receiver.getItemMeta();
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setDisplayName(ChatColor.AQUA + LanguageManager.getValue("receiver"));
        receiver.setItemMeta(meta);
        receiver.setDurability((short)112);
        return receiver;
    }

    public void updateReceiver(ItemStack receiver, int x, int y, int z) {
        if (receiver != null && receiver.equals(createTerminal())) {
            ItemMeta meta = receiver.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setLore(Arrays.asList(ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "Linked",
                                       ChatColor.GRAY.toString() + "X: " + x,
                                       ChatColor.GRAY.toString() + "Y: " + y,
                                       ChatColor.GRAY.toString() + "Z: " + z,
                                       "",
                                       ChatColor.GRAY + "Shift + Throw to Unlink"));
            receiver.setItemMeta(meta);
            receiver.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        }
    }
}
