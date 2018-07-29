package com.fadelands.core.profile;

import com.fadelands.array.Array;
import com.fadelands.core.CorePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileCommandExecutor implements CommandExecutor {

    public CorePlugin plugin;

    public ProfileCommandExecutor(CorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command may only be used in-game.");
            return false;
        }
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("profile")) {
            if (args.length == 0) {
                ProfileListener.openProfileInv(player);
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
                    player.sendMessage("§cThat player has never played on the server before.");
                } else {
                    targetStr = rs.getString("player_username");
                    ProfileListener.openOtherPlayerProfileInv(player, targetStr);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                player.sendMessage("§cAn error occurred while loading " + targetStr + "'s profile. If this error keeps persisting, please contact an administrator.");
            }finally {
                Array.closeComponents(rs, ps, connection);
            }
        }
        return false;
    }
}

