package com.fadelands.array.commands.admin;

import com.fadelands.array.database.DatabasePerformance;
import com.fadelands.array.Array;
import com.fadelands.array.utils.Utils;
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
                sender.sendMessage(Utils.No_Perm);
                return true;
            }
            new DatabasePerformance();
            sender.sendMessage("§2§lDatabase (" + DatabasePerformance.getObjectName() + ")");
            sender.sendMessage("§a§lConnections [IDLING, MAX] §f(" + DatabasePerformance.getIdleConnections() + "/" + DatabasePerformance.getTotalConnections() + ")");
            sender.sendMessage("§a§lConnections [ACTIVE, MAX] §f(" + DatabasePerformance.getActiveConnections() + "/" + DatabasePerformance.getTotalConnections() + ")");
            sender.sendMessage("§a§lThreads Awaiting Connection: §f" + DatabasePerformance.getWaitingThreads());

        return false;
    }
}
