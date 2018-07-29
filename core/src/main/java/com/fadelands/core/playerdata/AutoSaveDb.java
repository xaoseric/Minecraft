 package com.fadelands.core.playerdata;

import com.fadelands.array.Array;
import com.fadelands.array.utils.Perms;
import com.fadelands.array.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@SuppressWarnings("ALL")
public class AutoSaveDb extends BukkitRunnable {

    @Override
    public void run() {
        for (PlayerData playerdata : PlayerData.getAll()) {
            try(Connection connection = Array.getConnection()){
                try (PreparedStatement statement = connection.prepareStatement("UPDATE fadelands_stats_global SET " +
                        "messages_sent=?," +
                        "commands_used=?," +
                        "login_count=?," +
                        "blocks_placed_global=?," +
                        "blocks_removed_global=?," +
                        "deaths_global=?," +
                        "kills_global=? " +
                        "WHERE player_uuid='" + playerdata.getUUID() + "'")) {


                    statement.setInt(1, playerdata.getStats().getMessagesSent());
                    statement.setInt(2, playerdata.getStats().getCommandsUsed());
                    statement.setInt(3, playerdata.getStats().getLoginCount());
                    statement.setInt(4, playerdata.getStats().getBlocksPlaced());
                    statement.setInt(5, playerdata.getStats().getBlocksRemoved());
                    statement.setInt(6, playerdata.getStats().getDeaths());
                    statement.setInt(7, playerdata.getStats().getKills());

                    statement.executeUpdate();

                }
            } catch (SQLException e) {
                e.printStackTrace();

            }
        }

        for (Player admins : Bukkit.getOnlinePlayers()){
            if(admins.hasPermission(Perms.adminAlerts)) {
                admins.sendMessage(Utils.AdminPrefix + Utils.Prefix_Green + "§aAutosaved player data to database.");
            }
        }
    }
}