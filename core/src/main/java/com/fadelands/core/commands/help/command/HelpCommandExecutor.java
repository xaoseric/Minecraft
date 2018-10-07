package com.fadelands.core.commands.help.command;

import com.fadelands.core.CorePlugin;
import com.fadelands.core.commands.help.inventory.HelpInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommandExecutor implements CommandExecutor {

    public CorePlugin plugin;

    public HelpCommandExecutor(CorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used in-game.");
            return true;
        }
            Player player = (Player) sender;
            new HelpInventory(plugin).openInventory(player);
        return false;
    }
}

