package com.fadelands.essentials.commands.admin;

import com.fadelands.core.player.UserUtil;
import com.fadelands.core.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This can only be used in-game.");
            return true;
        }

        Player player = (Player) sender;
        if (!(UserUtil.isStaff(player.getName()))) {
            player.sendMessage(Utils.No_Perm);
            return true;
        }

        if (args.length == 0) {
            player.setFoodLevel(20);
            player.sendMessage(Utils.Prefix + "§aYou have been fed.");

        } else if (args.length == 1) {

            if (player.getServer().getPlayer(args[0]) != null) {
                Player target = player.getServer().getPlayer(args[0]);
                target.setFoodLevel(20);
                player.sendMessage(Utils.Prefix + "§aYou have fed " + target.getName() + ".");
                target.sendMessage(Utils.Prefix + "§aYou have been fed by " + player.getName() + ".");
            } else {
                player.sendMessage(Utils.Prefix + "§cThat player is not online.");
            }
        }
        return true;
    }
}