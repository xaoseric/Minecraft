package com.fadelands.lobby.commands;

import com.fadelands.lobby.Main;
import com.fadelands.lobby.Utils;
import com.fadelands.lobby.events.JoinItems;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildMode implements CommandExecutor {

    public Main plugin;
    public BuildMode(Main plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("This can only be used ingame.");
            return true;
        }
        Player player = (Player) sender;

        if(command.getName().equalsIgnoreCase("buildmode")){
            if(!player.hasPermission("fadelands.buildmode")){
                player.sendMessage(Utils.No_Perm);
                return false;
            }
            if(plugin.buildMode.contains(player)) {
                plugin.buildMode.remove(player);
                player.sendMessage(Utils.Prefix+ "§cYou toggled build mode off.");
                player.setGameMode(GameMode.SURVIVAL);
                JoinItems.getJoinItems(player);
            }else{
                plugin.buildMode.add(player);
                player.sendMessage(Utils.Prefix + "§aYou toggled build mode on.");
                player.setGameMode(GameMode.CREATIVE);
                player.getInventory().clear();
            }

        }
        return false;
    }
}
