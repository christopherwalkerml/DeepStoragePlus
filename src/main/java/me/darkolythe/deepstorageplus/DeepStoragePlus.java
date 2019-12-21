package me.darkolythe.deepstorageplus;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class DeepStoragePlus extends JavaPlugin {

    public static String prefix = ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "[" + ChatColor.BLUE.toString() + "DeepStorage" + ChatColor.WHITE.toString() + ChatColor.BOLD.toString() + "] ";

    @Override
    public void onEnable() {
        // Plugin startup logic

        System.out.println(prefix + ChatColor.GREEN + "DeepStoragePlus enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
