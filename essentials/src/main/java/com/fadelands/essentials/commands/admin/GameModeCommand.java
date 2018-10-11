package com.fadelands.essentials.commands.admin;

import com.fadelands.array.player.User;
import com.fadelands.array.utils.Utils;
import com.fadelands.essentials.Main;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameModeCommand implements CommandExecutor {

    public Main plugin;
    public GameModeCommand(Main plugin){
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("This can only be used ingame");
            return true;
        }
        Player player = (Player) sender;
        if(!(new User().isAdmin(player.getName()))){
            player.sendMessage(Utils.No_Perm);
            return true;
        }

        if(args.length == 0){
            player.sendMessage("§cYou have to select a gamemode.");
            return true;
        }

        if(args[0].equalsIgnoreCase("creative") || (args[0].equalsIgnoreCase("c") || (args[0].equalsIgnoreCase("1")))){
            player.setGameMode(GameMode.CREATIVE);
            player.sendMessage("§aYour gamemode has been updated.");
            return true;
        }

        if(args[0].equalsIgnoreCase("survival") || (args[0].equalsIgnoreCase("s") || (args[0].equalsIgnoreCase("0")))){
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage("§aYour gamemode has been updated.");
            return true;
        }

        if(args[0].equalsIgnoreCase("adventure") || (args[0].equalsIgnoreCase("a") || (args[0].equalsIgnoreCase("2")))){
            player.setGameMode(GameMode.ADVENTURE);
            player.sendMessage("§aYour gamemode has been updated.");
            return true;
        }

        if(args[0].equalsIgnoreCase("spectator") || (args[0].equalsIgnoreCase("sp") || (args[0].equalsIgnoreCase("3")))) {
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage("§aYour gamemode has been updated.");
            return true;
        }

        return false;

    }

}
