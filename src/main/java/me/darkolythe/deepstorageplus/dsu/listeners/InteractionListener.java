package me.darkolythe.deepstorageplus.dsu.listeners;

import me.darkolythe.deepstorageplus.utils.ItemList;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractionListener implements Listener {
    
    @EventHandler
    public void onJukeboxInteraction(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK){
            return;
        }
    
        Block block = event.getClickedBlock();
        if (block != null && block.getType() == Material.JUKEBOX) {
            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
            if (!event.isCancelled() && ItemList.isStorageContainerItem(item)) {
                event.setCancelled(true);
            }
        }
    }
}
