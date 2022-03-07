package me.darkolythe.deepstorageplus.utils.item.wrench;

import me.darkolythe.deepstorageplus.utils.ItemList;
import me.darkolythe.deepstorageplus.utils.item.DSPItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class SorterWrench implements DSPItem{
    
    private final ItemStack item;
    private final ItemMeta meta;
    
    public SorterWrench() {
        this.item = new ItemStack(Material.PAPER);
        this.meta = Objects.requireNonNull(item.getItemMeta());
        
        meta.setCustomModelData(ItemList.SORTER_WRENCH_MODEL_ID);
    }
    
    
    @Override
    public SorterWrench setName(String name) {
        meta.setDisplayName(name);
        return this;
    }
    
    @Override
    public SorterWrench setLore(List<String> lore) {
        meta.setLore(lore);
        return this;
    }
    
    @Override
    public SorterWrench setItemMeta() {
        item.setItemMeta(meta);
        return this;
    }
    
    @Override
    public ItemStack getItem() {
        return item;
    }
}
