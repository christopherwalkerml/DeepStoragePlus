package me.darkolythe.deepstorageplus.utils.item.storagecells;

import me.darkolythe.deepstorageplus.utils.item.DSPItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class StorageCell implements DSPItem{
    
    private final ItemStack item;
    private final ItemMeta meta;
    
    public StorageCell() {
        this.item = new ItemStack(Material.PAPER);
        this.meta = Objects.requireNonNull(item.getItemMeta());
    }
    
    @Override
    public StorageCell setName(String name) {
        meta.setDisplayName(name);
        return this;
    }
    
    @Override
    public StorageCell setLore(List<String> lore) {
        meta.setLore(lore);
        return this;
    }
    
    @Override
    public StorageCell setItemMeta() {
        item.setItemMeta(meta);
        return this;
    }
    
    @Override
    public ItemStack getItem() {
        return item;
    }
    
    public StorageCell setCustomModelData(int modelData) {
        meta.setCustomModelData(modelData);
        return this;
    }
}
