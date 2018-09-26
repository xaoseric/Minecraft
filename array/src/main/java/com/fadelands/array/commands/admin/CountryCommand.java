package com.fadelands.array.commands.admin;

import com.fadelands.array.Array;
import com.fadelands.array.player.User;
import com.fadelands.array.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CountryCommand implements CommandExecutor {

    public Array plugin;

    public CountryCommand(Array plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdlabel, String[] args) {
        Player player = (Player) sender;
        if (!(new User().isAdmin(player.getName()))) {
            sender.sendMessage(Utils.No_Perm);
            return true;
        }

        if(args.length == 0){
            sender.sendMessage(Utils.Prefix + "§cYou need to enter a username.");
            return true;
        }

        User user = new User();
        String target = args[0];
        if (!user.hasPlayedBefore(target)) {
            sender.sendMessage(Utils.Prefix + "§cThat's not a valid player.");
            return true;
        }

        String ip = user.getIp(target);

        sender.sendMessage(Utils.Prefix + "§2Country of §a" + target + "§2: " + plugin.getGeoManager().getCountry(ip));

        return false;
    }
}