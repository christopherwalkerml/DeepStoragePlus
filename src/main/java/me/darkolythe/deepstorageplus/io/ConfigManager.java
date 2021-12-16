package me.darkolythe.deepstorageplus.io;

import me.darkolythe.deepstorageplus.DeepStoragePlus;
import me.darkolythe.deepstorageplus.utils.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;

public class ConfigManager implements Listener {

    private DeepStoragePlus main;
    public ConfigManager(DeepStoragePlus plugin) {
        main = plugin;
    }
    private final String link = "https://mc-packs.net/download/59195941485ccb77792854e7e8e7d9d521822403";
    //MAKE SURE NOT TO ZIP WHOLE FOLDER. ONLY ZIP TWO FILES INSIDE

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        if (DeepStoragePlus.loadpack) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, () -> {
                event.getPlayer().setResourcePack(link);
            }, 1);
        }
    }

    @EventHandler
    private void onResourcePackClick(PlayerResourcePackStatusEvent event) {
        Status status = event.getStatus();
        if (status == Status.DECLINED || status == Status.FAILED_DOWNLOAD) {
            if (DeepStoragePlus.packmsg) {
                event.getPlayer().sendMessage(DeepStoragePlus.prefix + ChatColor.RED + LanguageManager.getValue("faileddownload"));
                event.getPlayer().sendMessage(DeepStoragePlus.prefix + ChatColor.GRAY + LanguageManager.getValue("downloadhere") + ": " + link);
            }
        }
    }
}
