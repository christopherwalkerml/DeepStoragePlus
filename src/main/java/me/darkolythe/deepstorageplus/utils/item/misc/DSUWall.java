package me.darkolythe.deepstorageplus.utils.item.misc;

import me.darkolythe.deepstorageplus.utils.ItemList;
import me.darkolythe.deepstorageplus.utils.item.DSPItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class DSUWall implements DSPItem{
    
    private final ItemStack item;
    private final ItemMeta meta;
    
    public DSUWall() {
        this.item = new ItemStack(Material.PAPER);
        this.meta = Objects.requireNonNull(item.getItemMeta());
        
        meta.setCustomModelData(ItemList.GUI_BACKGROUND_MODEL_ID);
    }
    
    @Override
    public DSUWall setName(String name) {
        meta.setDisplayName(name);
        return this;
    }
    
    @Override
    public DSUWall setLore(List<String> lore) {
        return this;
    }
    
    @Override
    public DSUWall setItemMeta() {
        item.setItemMeta(meta);
        return this;
    }
    
    @Override
    public ItemStack getItem() {
        return item;
    }
}
