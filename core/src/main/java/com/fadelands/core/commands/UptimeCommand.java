package com.fadelands.core.commands;

import com.fadelands.core.Core;
import com.fadelands.core.player.UserUtil;
import com.fadelands.core.utils.TimeUtils;
import com.fadelands.core.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UptimeCommand implements CommandExecutor {

    public Core plugin;
    public UptimeCommand(Core plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(!(UserUtil.isRedTag(player.getName()))) {
            sender.sendMessage(Utils.No_Perm);
            return true;

        }
        long uptime = plugin.getServerUptime();
        sender.sendMessage("§bTime since server booted or reloaded was " + TimeUtils.toRelative(uptime) + " ago.");
        return false;
    }
}
