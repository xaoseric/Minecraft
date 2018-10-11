package com.fadelands.array.commands.help.command;

import com.fadelands.array.Array;
import com.fadelands.array.commands.help.guides.GuideMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GuidesCommandExecutor implements CommandExecutor {

    public Array plugin;

    public GuidesCommandExecutor(Array plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used in-game.");
            return true;
        }
            Player player = (Player) sender;
            GuideMenu.openInventory(player);
        return false;
    }
}