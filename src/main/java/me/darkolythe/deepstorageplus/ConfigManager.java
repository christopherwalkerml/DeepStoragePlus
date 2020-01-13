package me.darkolythe.deepstorageplus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;

public class ConfigManager implements Listener {

    private DeepStoragePlus main;
    ConfigManager(DeepStoragePlus plugin) {
        main = plugin;
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
            @Override
            public void run() {
                event.getPlayer().setResourcePack("https://drive.google.com/uc?export=download&id=1bjolAQLCQsYyNi6uXZfthMVUbr46JzjB");
            }
        }, 1);
    }

    @EventHandler
    private void onResourcePackClick(PlayerResourcePackStatusEvent event) {
        Status status = event.getStatus();
        if (status == Status.DECLINED || status == Status.FAILED_DOWNLOAD) {
            event.getPlayer().sendMessage(DeepStoragePlus.prefix + ChatColor.RED + LanguageManager.getValue("faileddownload"));
            event.getPlayer().sendMessage(DeepStoragePlus.prefix + ChatColor.GRAY + LanguageManager.getValue("downloadhere") + ": https://drive.google.com/uc?export=download&id=1bjolAQLCQsYyNi6uXZfthMVUbr46JzjB");
        }
    }
}
