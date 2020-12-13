package me.darkolythe.deepstorageplus.io;

import me.darkolythe.deepstorageplus.DeepStoragePlus;
import me.darkolythe.deepstorageplus.utils.ItemList;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
        if (sender instanceof Player) {
            // The following command should give the sender 6 StorageCell16K
            // give storagecell16k 6
            // TODO: Need to check permissions here
            if (args.length >= 2 && args[0].equalsIgnoreCase("give")) {
                Optional<Player> player = Bukkit.getServer().getOnlinePlayers().stream().map(x -> (Player) x).filter(x -> x.getDisplayName().equalsIgnoreCase(args[1])).findAny();
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
                    if (args.length >= 3) {
                        itemName = args[1];
                        quantity = StringUtils.isNumeric(args[2]) ? Integer.parseInt(args[2]) : 1;
                    }
                    else if (args.length >= 2) {
                        itemName = args[1];
                    }
                }
                Optional<ItemStack> item = itemList.getItem(itemName);
                if (item.isPresent()) {
                    for (int i = 0; i < quantity; i++) {
                        player.orElse((Player) sender).getInventory().addItem(item.get());
                    }
                } else {
                    sender.sendMessage("Invalid or missing item name or recipient provided");
                }
            }
        }
        return true;
    }
}
