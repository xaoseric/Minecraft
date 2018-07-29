package com.fadelands.array.commands.admin;

import com.fadelands.array.database.DatabasePerformance;
import com.fadelands.array.Array;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DatabaseStatusCommandExecutor implements CommandExecutor {

    public Array plugin;
    public DatabaseStatusCommandExecutor(Array plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            if(!(sender.isOp()) || (!(sender.hasPermission("fadelands.databasestatus")))){
                sender.sendMessage("You do not have access to this command.");
                return true;
            }
            new DatabasePerformance();
            sender.sendMessage("§2Database (" + DatabasePerformance.getObjectName() + ")");
            sender.sendMessage("§aConnections [IDLING, MAX] (" + DatabasePerformance.getIdleConnections() + "/" + DatabasePerformance.getTotalConnections() + ")");
            sender.sendMessage("§aConnections [ACTIVE, MAX] (" + DatabasePerformance.getActiveConnections() + "/" + DatabasePerformance.getTotalConnections() + ")");
            sender.sendMessage("§aThreads Awaiting Connection: " + DatabasePerformance.getWaitingThreads());

        return false;
    }
}
