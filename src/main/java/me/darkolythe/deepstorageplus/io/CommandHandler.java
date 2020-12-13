package me.darkolythe.deepstorageplus.io;

import me.darkolythe.deepstorageplus.DeepStoragePlus;
import me.darkolythe.deepstorageplus.utils.ItemList;
import org.apache.commons.lang.StringUtils;
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
            if (args.length >= 2 && args[0].equalsIgnoreCase("give")) {
                getItem(args[1]).ifPresent(x -> {
                    if (args.length >= 3 && args[2].length() > 0 && StringUtils.isNumeric(args[2])) {
                        for (int i = 0; i < Integer.parseInt(args[2]); i++ ){
                            ((Player) sender).getInventory().addItem(x);
                        }
                    }
                    else {
                        ((Player) sender).getInventory().addItem(x);
                    }
                });
            }
        }

        return true;
    }

    private Optional<ItemStack> getItem(String itemName) {
        ItemStack item = null;
        switch (itemName.toLowerCase()) {
            case "storagecell1k": item = itemList.storageCell1K; break;
            case "storagecell4k": item = itemList.storageCell4K; break;
            case "storagecell16k": item = itemList.storageCell16K; break;
            case "storagecell64k": item = itemList.storageCell64K; break;
            case "storagecell256k": item = itemList.storageCell256K; break;
            case "storagecell1m": item = itemList.storageCell1M; break;
            case "storagecontainer1k": item = itemList.storageContainer1K; break;
            case "storagecontainer4k": item = itemList.storageContainer4K; break;
            case "storagecontainer16k": item = itemList.storageContainer16K; break;
            case "storagecontainer64k": item = itemList.storageContainer64K; break;
            case "storagecontainer256k": item = itemList.storageContainer256K; break;
            case "storagecontainer1m": item = itemList.storageContainer1M; break;
            case "wrench": item = itemList.wrench; break;
            case "receiver": item = itemList.receiver; break;
            case "terminal": item = itemList.terminal; break;
            case "speedupgrade": item = itemList.speedUpgrade; break;
        }
        return Optional.ofNullable(item);
    }
}
