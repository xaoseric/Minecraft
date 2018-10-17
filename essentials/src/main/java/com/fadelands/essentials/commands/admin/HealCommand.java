package com.fadelands.essentials.commands.admin;

import com.fadelands.core.player.UserUtil;
import com.fadelands.core.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {

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
            player.setHealth(20);
            player.setFoodLevel(20);
            player.setFireTicks(0);
            player.sendMessage(Utils.Prefix + "§aYou have been healed.");

        } else if (args.length == 1) {

            if (player.getServer().getPlayer(args[0]) != null) {
                Player target = player.getServer().getPlayer(args[0]);
                target.setHealth(20);
                target.setFoodLevel(20);
                target.setFireTicks(0);
                player.sendMessage(Utils.Prefix + "§aYou have healed " + target.getName() + ".");
                target.sendMessage(Utils.Prefix + "§aYou have been healed by " + player.getName() + ".");
            } else {
                player.sendMessage(Utils.Prefix + "§cThat player is not online.");
            }
        }
        return true;
    }
}
