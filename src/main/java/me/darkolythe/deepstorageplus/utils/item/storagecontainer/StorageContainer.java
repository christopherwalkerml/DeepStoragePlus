package me.darkolythe.deepstorageplus.utils.item.storagecontainer;

import me.darkolythe.deepstorageplus.utils.LanguageManager;
import me.darkolythe.deepstorageplus.utils.item.DSPItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StorageContainer implements DSPItem{
    
    private final ItemStack item;
    private final ItemMeta meta;
    
    public StorageContainer() {
        this.item = new ItemStack(Material.IRON_HORSE_ARMOR);
        this.meta = Objects.requireNonNull(item.getItemMeta());
    }
    
    @Override
    public StorageContainer setName(String name) {
        meta.setDisplayName(name);
        return this;
    }
    
    @Override
    public StorageContainer setLore(List<String> lore) {
        meta.setLore(lore);
        return this;
    }
    
    @Override
    public StorageContainer setItemMeta() {
        item.setItemMeta(meta);
        return this;
    }
    
    @Override
    public ItemStack getItem() {
        return item;
    }
    
    public StorageContainer setCustomModelData(int modelData){
        meta.setCustomModelData(modelData);
        return this;
    }
    
    public StorageContainer setLore(int maxSize) {
        List<String> lore = new ArrayList<>();
        
        lore.add(ChatColor.GREEN + LanguageManager.getValue("currentstorage") + ": " + 0 + "/" + maxSize);
        lore.add(ChatColor.GREEN + LanguageManager.getValue("currenttypes") + ": " + 0 + "/7");
        
        for (int i = 0; i < 7; i++) {
            lore.add(ChatColor.GRAY + " - " + LanguageManager.getValue("empty"));
        }
        
        return setLore(lore);
    }
}
