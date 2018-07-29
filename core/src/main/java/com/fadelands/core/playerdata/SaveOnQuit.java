package com.fadelands.core.playerdata;

import com.fadelands.array.Array;
import com.fadelands.core.CorePlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SaveOnQuit implements Listener {

    public CorePlugin plugin;
    public SaveOnQuit(CorePlugin plugin){
        this.plugin = plugin;

    }
    @EventHandler
    public void saveOnQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
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
                System.out.println("Saved player data of " + player.getName());

            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Failed to save player data of " + player.getName());

            }
        }
    }
}
