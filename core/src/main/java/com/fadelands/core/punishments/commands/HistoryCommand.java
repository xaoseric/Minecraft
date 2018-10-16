package com.fadelands.core.punishments.commands;

import com.fadelands.core.Core;
import com.fadelands.core.punishments.*;
import com.fadelands.core.player.User;
import com.fadelands.core.punishments.inventory.HistoryInventory;
import com.fadelands.core.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class HistoryCommand implements CommandExecutor {

    private Core plugin;

    public HistoryCommand(Core plugin, PunishmentManager punishmentManager){
        this.plugin = plugin;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Utils.No_Console);
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) { // Get own punishment history
            new HistoryInventory(plugin).openInventory(player, player.getName());
            return true;
        }

        if (!User.isMod(player.getName())) {
            sender.sendMessage(Utils.No_Perm);
            return true;
        }

        String target = args[0];
        if (!(User.hasPlayedBefore(target))) {
            player.sendMessage(Utils.Prefix + "§cCouldn't find that user.");
            return true;
        }

        new HistoryInventory(plugin).openInventory(player, target);

        return false;
    }
}
