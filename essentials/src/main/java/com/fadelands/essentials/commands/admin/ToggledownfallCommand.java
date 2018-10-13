package com.fadelands.essentials.commands.admin;

import com.fadelands.core.player.User;
import com.fadelands.core.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggledownfallCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Utils.No_Console);
            return true;
        }

        Player player = (Player) sender;
        if(!(User.isRedTag(player.getName()))) {
            player.sendMessage(Utils.No_Perm);
            return true;
        }

        if(args.length == 0) {
            World world = player.getWorld();
            if(world.hasStorm()) {
                world.setStorm(false);
            } else {
                world.setStorm(true);
            }
            player.sendMessage(Utils.Prefix + "§2Toggled downfall.");
            return true;
        }

        String worldStr = args[0];
        World world = Bukkit.getServer().getWorld(worldStr);
        if(world == null) {
            player.sendMessage(Utils.Prefix + "§cCouldn't find the world " + worldStr + ".");
            return true;
        }

        if(world.hasStorm()) {
            world.setStorm(false);
        } else {
            world.setStorm(true);
        }
        player.sendMessage(Utils.Prefix + "§2Toggled downfall in " + world.getName() + ".");
        return true;
    }
}
