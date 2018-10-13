package com.fadelands.core.commands;

import com.fadelands.core.Core;
import com.fadelands.core.database.DatabasePerformance;
import com.fadelands.core.player.User;
import com.fadelands.core.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DatabaseStatusCommand implements CommandExecutor {

    public Core plugin;
    public DatabaseStatusCommand(Core plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if(!(User.isRedTag(sender.getName()))) {
                sender.sendMessage(Utils.No_Perm);
                return true;
            }
            new DatabasePerformance();
            sender.sendMessage("§3Database (" + DatabasePerformance.getObjectName() + ")");
            sender.sendMessage("§bConnections [IDLING, MAX] §f(" + DatabasePerformance.getIdleConnections() + "/" + DatabasePerformance.getTotalConnections() + ")");
            sender.sendMessage("§bConnections [ACTIVE, MAX] §f(" + DatabasePerformance.getActiveConnections() + "/" + DatabasePerformance.getTotalConnections() + ")");
            sender.sendMessage("§bThreads Awaiting Connection: §f" + DatabasePerformance.getWaitingThreads());

        return false;
    }
}
