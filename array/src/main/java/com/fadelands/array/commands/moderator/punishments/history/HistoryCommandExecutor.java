package com.fadelands.array.commands.moderator.punishments.history;

import com.fadelands.array.Array;
import com.fadelands.array.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HistoryCommandExecutor implements CommandExecutor {

    private Array array;

    public HistoryCommandExecutor(Array array){
        this.array = array;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
       if(!(commandSender instanceof Player)){
           commandSender.sendMessage("§cThis command can only be used ingame.");
           return true;
       }
       Player player = (Player) commandSender;
       if(!player.hasPermission("fadelands.history")){
           player.sendMessage(Utils.No_Perm);
           return true;
       }

       if(args.length == 0){
           player.sendMessage(Utils.Prefix_Red + "§cYou are missing a few parameters. /history <target>");
           return true;
       }
       

        return false;
    }                                                                          
}
