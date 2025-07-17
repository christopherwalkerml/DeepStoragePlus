package me.darkolythe.deepstorageplus.io;

import me.darkolythe.deepstorageplus.DeepStoragePlus;
import me.darkolythe.deepstorageplus.utils.ItemList;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Optional;

public class CommandHandler implements CommandExecutor {

    private DeepStoragePlus main = DeepStoragePlus.getInstance();

    private ItemList itemList;

    public CommandHandler(ItemList itemList) {
        super();
        this.itemList = itemList;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandlabel, String[] args) {
        if (sender instanceof Player || sender instanceof BlockCommandSender) {
            // Example give commands
            // give storagecell16k 6
            // give joe storagecontainer1M 2
            // give wrench
            if (args.length == 1 && args[0].equalsIgnoreCase("items") && sender.hasPermission("deepstorageplus.give")) {
                String items = "";
                for (String s : itemList.itemListMap.keySet()) {
                    items += ChatColor.GREEN + s + ChatColor.BLUE + ", ";
                }
                sender.sendMessage(DeepStoragePlus.prefix + items);
            } else if (args.length >= 2 && args[0].equalsIgnoreCase("give") && (sender instanceof BlockCommandSender || sender.hasPermission("deepstorageplus.give"))) {
                Optional<Player> player = Bukkit.getServer().getOnlinePlayers().stream().map(x -> (Player) x).filter(x -> x.getName().equalsIgnoreCase(args[1])).findAny();
                String itemName = null;
                int quantity = 1;
                if (player.isPresent()) { // A recipient player was specified
                    if (args.length >= 4) {
                        itemName = args[2];
                        quantity = StringUtils.isNumeric(args[3]) ? Integer.parseInt(args[3]) : 1;
                    }
                    else if (args.length >= 3) {
                        itemName = args[2];
                    }
                } else {
                    if (sender instanceof BlockCommandSender) {
                        return false;
                    }

                    if (args.length >= 3) {
                        itemName = args[1];
                        quantity = StringUtils.isNumeric(args[2]) ? Integer.parseInt(args[2]) : 1;
                    }
                    else {
                        itemName = args[1];
                    }
                }
                Optional<ItemStack> item = itemList.getItem(itemName);
                if (item.isPresent()) {
                    for (int i = 0; i < quantity; i++) {
                        sender.sendMessage(DeepStoragePlus.prefix + ChatColor.GREEN + "given " + itemName + " to " + player.orElseGet(() -> (Player) sender).getName());
                        player.orElseGet(() -> (Player) sender).getInventory().addItem(item.get());
                    }
                } else {
                    sender.sendMessage(DeepStoragePlus.prefix + ChatColor.RED + "Invalid Arguments: /dsp give <user> item <amt>");
                    return true;
                }
            } else {
                if (sender.hasPermission("deepstorageplus.give")) {
                    sender.sendMessage(DeepStoragePlus.prefix + ChatColor.RED + "Invalid Arguments: /dsp [(give <user> item <amt>), items]");
                } else {
                    sender.sendMessage(DeepStoragePlus.prefix + ChatColor.RED + "No permissions");
                }
            }
        }
        return true;
    }
}
