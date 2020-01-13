package me.darkolythe.deepstorageplus;

import me.darkolythe.deepstorageplus.DeepStoragePlus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    private DeepStoragePlus main = DeepStoragePlus.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {

        if (sender instanceof Player) {

        }

        return true;
    }
}
