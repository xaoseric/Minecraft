package com.fadelands.array.playerdata;

import com.fadelands.array.Array;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class LoadPlayerData implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        new PlayerData(uuid);
        load(uuid);
    }

    private static void load(UUID uuid) {

        try(Connection connection = Array.plugin.getDatabaseManager().getConnection()){
            try(PreparedStatement st = connection.prepareStatement("SELECT * FROM `stats_global` WHERE `player_uuid`='" + uuid.toString() + "'")) {
                try (ResultSet resultPlayerStats = st.executeQuery()) {

                    if (resultPlayerStats.next()) {

                        PlayerData.Statistics stats = new PlayerData(uuid).getStats();
                        stats.setMessagesSent(resultPlayerStats.getInt("messages_sent"));
                        stats.setCommandsUsed(resultPlayerStats.getInt("commands_used"));
                        stats.setLoginCount(resultPlayerStats.getInt("login_count"));
                        stats.setBlocksPlaced(resultPlayerStats.getInt("blocks_placed_global"));
                        stats.setBlocksRemoved(resultPlayerStats.getInt("blocks_removed_global"));
                        stats.setDeaths(resultPlayerStats.getInt("deaths_global"));
                        stats.setKills(resultPlayerStats.getInt("kills_global"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
