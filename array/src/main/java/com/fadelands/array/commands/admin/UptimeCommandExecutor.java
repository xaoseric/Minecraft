package com.fadelands.array.commands.admin;

import com.fadelands.array.Array;
import com.fadelands.array.utils.TimeUtils;
import com.fadelands.array.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UptimeCommandExecutor implements CommandExecutor {

    public Array plugin;
    public UptimeCommandExecutor(Array plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender.isOp()) || (!(sender.hasPermission("fadelands.uptime")))){
            sender.sendMessage(Utils.No_Perm);
            return true;

        }
        long uptime = plugin.getServerUptime();
        sender.sendMessage("§bTime since server booted or reloaded was " + TimeUtils.toRelative(uptime) + " ago.");
        return false;
    }
}
