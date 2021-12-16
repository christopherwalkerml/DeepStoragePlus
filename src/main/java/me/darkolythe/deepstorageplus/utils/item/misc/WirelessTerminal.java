package me.darkolythe.deepstorageplus.utils.item.misc;

import me.darkolythe.deepstorageplus.utils.ItemList;
import me.darkolythe.deepstorageplus.utils.item.DSPItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class WirelessTerminal implements DSPItem{
    
    private final ItemStack item;
    private final ItemMeta meta;
    
    public WirelessTerminal() {
        this.item = new ItemStack(Material.PAPER);
        this.meta = Objects.requireNonNull(item.getItemMeta());
        
        this.meta.setCustomModelData(ItemList.TERMINAL_MODEL_ID);
    }
    
    @Override
    public WirelessTerminal setName(String name) {
        meta.setDisplayName(name);
        return this;
    }
    
    @Override
    public WirelessTerminal setLore(List<String> lore) {
        meta.setLore(lore);
        return this;
    }
    
    @Override
    public WirelessTerminal setItemMeta() {
        item.setItemMeta(meta);
        return this;
    }
    
    @Override
    public ItemStack getItem() {
        return item;
    }
    
    public WirelessTerminal hideEnchantment() {
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }
    
    public WirelessTerminal setEnchanted() {
        item.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
        return this;
    }
}
