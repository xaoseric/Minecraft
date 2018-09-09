package com.fadelands.array.commands.admin;

import com.fadelands.array.database.DatabasePerformance;
import com.fadelands.array.Array;
import com.fadelands.array.player.User;
import com.fadelands.array.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DatabaseStatusCommand implements CommandExecutor {

    public Array plugin;
    public DatabaseStatusCommand(Array plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if(!(new User().isRedTag(sender.getName()))) {
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
