package me.darkolythe.deepstorageplus.utils.item.misc;

import me.darkolythe.deepstorageplus.utils.ItemList;
import me.darkolythe.deepstorageplus.utils.item.DSPItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class WirelessReceiver implements DSPItem{
    
    private final ItemStack item;
    private final ItemMeta meta;
    
    public WirelessReceiver() {
        this.item = new ItemStack(Material.PAPER);
        this.meta = Objects.requireNonNull(item.getItemMeta());
        
        this.meta.setCustomModelData(ItemList.RECEIVER_MODEL_ID);
    }
    
    @Override
    public WirelessReceiver setName(String name) {
        meta.setDisplayName(name);
        return this;
    }
    
    @Override
    public WirelessReceiver setLore(List<String> lore) {
        meta.setLore(lore);
        return this;
    }
    
    @Override
    public WirelessReceiver setItemMeta() {
        item.setItemMeta(meta);
        return this;
    }
    
    @Override
    public ItemStack getItem() {
        return item;
    }
}
