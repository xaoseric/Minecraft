package com.fadelands.dailyrewards.rewardman.command;

import com.fadelands.core.player.User;
import com.fadelands.core.utils.Utils;
import com.fadelands.dailyrewards.Main;
import com.fadelands.dailyrewards.rewardman.RewardMan;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RewardManCommand implements CommandExecutor {

    private Main plugin;

    public RewardManCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§4This command may only be used ingame.");
            return true;
        }

        Player player = (Player) sender;
        if (!(User.isAdmin(player.getName()))) {
            player.sendMessage(Utils.No_Perm);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Utils.Prefix + "§cMissing arguments. /rewardman [spawn/remove]");
            return true;
        }

        if (args[0].equalsIgnoreCase("spawn")) {

            Location location = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
            new RewardMan(plugin).spawnRewardMan(location, player);
            return true;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            new RewardMan(plugin).removeRewardMan(player);
            return true;
        }
        return false;
    }
}
