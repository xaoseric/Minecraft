package com.fadelands.sbair.skyblockmanager;

import com.fadelands.core.utils.Utils;
import com.fadelands.sbair.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IslandManagerCommand implements CommandExecutor {

    public Main plugin;

    public IslandManagerCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("This command can only be used ingame.");
            return true;
        }
            Player player = (Player) sender;
            if(!plugin.getSkyBlockApi().hasIsland(player.getUniqueId()) || (!plugin.getSkyBlockApi().inTeam(player.getUniqueId()))) {
                player.sendMessage(Utils.Prefix + "§cYou must be in a team or own an island to use the menu.");
                return true;
            }

            IslandMenu menu = new IslandMenu(plugin);
            menu.openIslandMenu(player);

            return false;
        }
}
