package com.fadelands.core.commands.staff.modcmds;

import com.fadelands.array.player.User;
import com.fadelands.core.CorePlugin;
import com.fadelands.array.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.List;

public class FlyCommandExecutor implements CommandExecutor {

    List<String> flying = new ArrayList<>();

    public CorePlugin plugin;
    public FlyCommandExecutor(CorePlugin plugin){
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("This can only be used ingame.");
            return true;
        }
        Player player = (Player) sender;
        if(!(new User().isMod(player.getName()))){
            player.sendMessage(Utils.No_Perm);
            return true;
        }

        if(!flying.contains(player.getName())){
            player.sendMessage("§aYou have toggled flight mode on.");
            player.setAllowFlight(true);
            player.setFlying(true);

            flying.add(player.getName());
        }else{
            player.sendMessage("§cYou have toggled flight mode off.");
            player.setFlying(false);
            player.setAllowFlight(false);

            flying.remove(player.getName());
        }

        return false;
    }
}
