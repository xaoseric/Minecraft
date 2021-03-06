package com.fadelands.essentials.commands.admin;

import com.fadelands.core.Core;
import com.fadelands.core.player.UserUtil;
import com.fadelands.core.utils.Utils;
import com.fadelands.essentials.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CountryCommand implements CommandExecutor {

    public Main plugin;

    public CountryCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String cmdlabel, String[] args) {
        Player player = (Player) sender;
        if (!(UserUtil.isAdmin(player.getName()))) {
            sender.sendMessage(Utils.No_Perm);
            return true;
        }

        if(args.length == 0){
            sender.sendMessage(Utils.Prefix + "§cYou need to enter a username.");
            return true;
        }

        String target = args[0];
        if (!UserUtil.hasPlayedBefore(target)) {
            sender.sendMessage(Utils.Prefix + "§cThat's not a valid player.");
            return true;
        }

        String ip = UserUtil.getIp(target);

        sender.sendMessage(Utils.Prefix + "§2Country of §a" + target + "§2: " + Core.plugin.getGeoManager().getCountry(ip));

        return false;
    }
}