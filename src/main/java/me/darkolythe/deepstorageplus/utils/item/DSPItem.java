package me.darkolythe.deepstorageplus.utils.item;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface DSPItem {
    
    public DSPItem setName(String name);
    
    public DSPItem setLore(List<String> lore);
    
    public DSPItem setItemMeta();
    
    public ItemStack getItem();
}
