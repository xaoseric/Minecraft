package com.fadelands.core.profile.command;

import com.fadelands.core.Core;
import com.fadelands.core.player.User;
import com.fadelands.core.utils.Utils;
import com.fadelands.core.profile.inventory.ProfileInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileCommand implements CommandExecutor {

    //todo: revamp the entire fucking code

    public Core plugin;

    public ProfileCommand(Core plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command may only be used in-game.");
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
                new ProfileInventory(plugin).openProfileInventory(player, player.getName());
                return false;
            }

            String targetStr = args[0];

           if(!(User.hasPlayedBefore(targetStr))) {
               player.sendMessage(Utils.Prefix + "§cCouldn't find that player.");
               return true;
           }
           new ProfileInventory(plugin).openProfileInventory(player, targetStr);
        return false;
    }
}

