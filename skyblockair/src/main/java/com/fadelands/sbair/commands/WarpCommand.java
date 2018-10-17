package com.fadelands.sbair.commands;

import com.fadelands.core.utils.Utils;
import com.fadelands.sbair.inventories.WarpInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(Utils.No_Console);
            return true;
        }

        Player player = (Player) sender;

        new WarpInventory().openInventory(player);

        return false;
    }
}
