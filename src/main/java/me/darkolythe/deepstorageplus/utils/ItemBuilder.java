package me.darkolythe.deepstorageplus.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {
    
    private final ItemStack item;
    private final ItemMeta meta;
    
    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }
    
    public ItemBuilder setName(String name) {
        meta.setDisplayName(name);
        return this;
    }
    
    public ItemBuilder setLore(int maxSize) {
        List<String> lore = new ArrayList<>();
        
        lore.add(ChatColor.GREEN + LanguageManager.getValue("currentstorage") + ": " + 0 + "/" + maxSize);
        lore.add(ChatColor.GREEN + LanguageManager.getValue("currenttypes") + ": " + 0 + "/" + 7);
        
        for (int i = 0; i < 7; i++) {
            lore.add(ChatColor.GRAY + " - " + LanguageManager.getValue("empty"));
        }
        
        this.setLore(lore);
        return this;
    }
    
    public ItemBuilder setLore(List<String> lore) {
        meta.setLore(lore);
        return this;
    }
    
    public ItemBuilder setModelData(int modelData) {
        meta.setCustomModelData(modelData);
        return this;
    }
    
    public ItemBuilder setUnbreakable() {
        meta.setUnbreakable(true);
        return this;
    }
    
    public ItemBuilder setEnchanted() {
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        return this;
    }
    
    public ItemBuilder setFlags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }
    
    public ItemBuilder setItemMeta() {
        item.setItemMeta(meta);
        return this;
    }
    
    public ItemStack getItem() {
        return item;
    }
}
