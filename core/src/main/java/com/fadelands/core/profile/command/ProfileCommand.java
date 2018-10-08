package com.fadelands.core.profile.command;

import com.fadelands.array.Array;
import com.fadelands.array.utils.Utils;
import com.fadelands.core.CorePlugin;
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

    public CorePlugin plugin;

    public ProfileCommand(CorePlugin plugin) {
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
            Connection connection = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            //noinspection Duplicates
            try {
                connection = Array.getConnection();
                ps = connection.prepareStatement("SELECT * FROM fadelands_players WHERE player_username='" + targetStr + "'");
                rs = ps.executeQuery();
                if (!rs.next()) {
                    player.sendMessage(Utils.Prefix + "§cI couldn't find that player.");
                } else {
                    targetStr = rs.getString("player_username");
                    new ProfileInventory(plugin).openProfileInventory(player, targetStr);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                player.sendMessage(Utils.Prefix + "§cAn error occurred while loading " + targetStr + "'s profile. If this error keeps persisting, please contact an administrator.");
            }finally {
                Array.closeComponents(rs, ps, connection);
            }
        return false;
    }
}

