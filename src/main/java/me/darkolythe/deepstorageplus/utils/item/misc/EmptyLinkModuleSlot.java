package me.darkolythe.deepstorageplus.utils.item.misc;

import me.darkolythe.deepstorageplus.utils.ItemList;
import me.darkolythe.deepstorageplus.utils.item.DSPItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class EmptyLinkModuleSlot implements DSPItem {
    
    private final ItemStack item;
    private final ItemMeta meta;
    
    public EmptyLinkModuleSlot() {
        this.item = new ItemStack(Material.PAPER);
        this.meta = Objects.requireNonNull(item.getItemMeta());
        
        meta.setCustomModelData(ItemList.LINK_SLOT_MODEL_ID);
    }
    
    @Override
    public EmptyLinkModuleSlot setName(String name) {
        meta.setDisplayName(name);
        return this;
    }
    
    @Override
    public EmptyLinkModuleSlot setLore(List<String> lore) {
        return this;
    }
    
    @Override
    public EmptyLinkModuleSlot setItemMeta() {
        item.setItemMeta(meta);
        return this;
    }
    
    @Override
    public ItemStack getItem() {
        return item;
    }
}
