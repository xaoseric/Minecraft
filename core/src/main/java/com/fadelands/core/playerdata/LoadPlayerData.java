package com.fadelands.core.playerdata;

import com.fadelands.array.Array;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoadPlayerData implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        new PlayerData(player.getUniqueId());
        load(player);
        player.sendMessage("§2✔ §aYour user data has been loaded.");
    }

    private static void load(Player player) {

        try(Connection connection = Array.getConnection()){
            try(PreparedStatement st = connection.prepareStatement("SELECT * FROM `fadelands_stats_global` WHERE `player_uuid`='" + player.getUniqueId().toString() + "'")) {
                try (ResultSet resultPlayerStats = st.executeQuery()) {

                    if (resultPlayerStats.next()) {

                        PlayerData.Statistics stats = new PlayerData(player.getUniqueId()).getStats();
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
            player.sendMessage("§4✖ §cCouldn't load your user data. If this issues persists, report it to a staff member.");
        }
    }
}
