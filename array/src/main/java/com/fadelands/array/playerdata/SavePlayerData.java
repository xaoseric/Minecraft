package com.fadelands.array.playerdata;

import com.fadelands.array.Array;
import com.fadelands.array.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SavePlayerData implements CommandExecutor {

    public Array plugin;

    public SavePlayerData(Array plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command may only be used in-game.");
            return false;
        }
        if (cmd.getName().equalsIgnoreCase("saveplayerdata")) {
            Player player = (Player) sender;
            if(!sender.hasPermission("fadelands.saveplayerdata")){
                sender.sendMessage(Utils.No_Perm);
                return false;
            }

            for (PlayerData playerdata : PlayerData.getAll()) {
                try(Connection connection = plugin.getDatabaseManager().getConnection()){
                    try(PreparedStatement statement = connection.prepareStatement("UPDATE stats_global SET " +
                            "messages_sent=?," +
                            "commands_used=?," +
                            "login_count=?," +
                            "blocks_placed_global=?," +
                            "blocks_removed_global=?," +
                            "deaths_global=?," +
                            "kills_global=? " +
                            " WHERE player_uuid='" + playerdata.getUUID() + "'")) {
                        statement.setInt(1, playerdata.getStats().getMessagesSent());
                        statement.setInt(2, playerdata.getStats().getCommandsUsed());
                        statement.setInt(3, playerdata.getStats().getLoginCount());
                        statement.setInt(4, playerdata.getStats().getBlocksPlaced());
                        statement.setInt(5, playerdata.getStats().getBlocksRemoved());
                        statement.setInt(6, playerdata.getStats().getDeaths());
                        statement.setInt(7, playerdata.getStats().getKills());

                        statement.executeUpdate();
                    }
                    player.sendMessage(Utils.Prefix_Green + "§aPlayer Data has been saved in the database.");

                } catch (SQLException e) {
                    e.printStackTrace();

                }
            }


        }

    return false;
    }
}

