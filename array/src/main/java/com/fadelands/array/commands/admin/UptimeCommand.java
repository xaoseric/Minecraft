package com.fadelands.array.commands.admin;

import com.fadelands.array.Array;
import com.fadelands.array.player.User;
import com.fadelands.array.utils.TimeUtils;
import com.fadelands.array.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UptimeCommand implements CommandExecutor {

    public Array plugin;
    public UptimeCommand(Array plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = new User();
        Player player = (Player) sender;
        if(!(user.isRedTag(player.getName()))) {
            sender.sendMessage(Utils.No_Perm);
            return true;

        }
        long uptime = plugin.getServerUptime();
        sender.sendMessage("§bTime since server booted or reloaded was " + TimeUtils.toRelative(uptime) + " ago.");
        return false;
    }
}
