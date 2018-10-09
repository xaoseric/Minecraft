package com.fadelands.bungeecore.players;

import com.fadelands.bungeecore.Main;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class BungeeCommandsLogging implements Listener {

    public Main plugin;

    public BungeeCommandsLogging(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerChatEvent(ChatEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        String cmd = event.getMessage();

        if (event.isCommand()) {

            try(Connection connection = Main.getConnection()){
               try(PreparedStatement insert = connection
                        .prepareStatement("INSERT INTO chat_commands (player_uuid,player_username,server,date,commands) VALUE (?,?,?,?,?)")) {

                   Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                   insert.setString(1, player.getUniqueId().toString());
                   insert.setString(2, player.getName());
                   insert.setString(3, player.getServer().getInfo().getName());
                   insert.setTimestamp(4, new Timestamp(new DateTime(DateTimeZone.UTC).getMillis()));
                   insert.setString(5, cmd);
                   insert.executeUpdate();
               }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}