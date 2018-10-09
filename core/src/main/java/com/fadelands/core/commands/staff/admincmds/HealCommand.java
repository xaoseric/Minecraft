package com.fadelands.core.commands.staff.admincmds;

import com.fadelands.array.player.User;
import com.fadelands.array.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("This can only be used in-game.");
        }

        Player player = (Player) sender;
        if (!(new User().isAdmin(player.getName()))) {
            player.sendMessage(Utils.No_Perm);
            return true;
        }

        if (args.length == 0) {
            player.setHealth(20);
            player.setFoodLevel(20);
            player.setFireTicks(0);
            player.sendMessage("§aYou have been healed.");

        } else if (args.length == 1) {

            if (player.getServer().getPlayer(args[0]) != null) {
                Player target = player.getServer().getPlayer(args[0]);
                player.setHealth(20);
                player.setFoodLevel(20);
                player.setFireTicks(0);
                player.sendMessage("§aYou have healed " + target.getName());
                target.sendMessage("§aYou have been healed by" + player.getName());

            } else {
                player.sendMessage("§cThat player is not online.");
            }


        }
        return true;
    }
}
