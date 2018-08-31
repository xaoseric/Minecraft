package com.fadelands.bungeecore.pm;

import com.fadelands.bungeecore.Main;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PMManager {
    private static Map<String, String> lastMessage = new HashMap<>();
    static ProxiedPlayer getLast(ProxiedPlayer player) {return ProxyServer.getInstance().getPlayer(lastMessage.get(player.getName()));}
    static void setLast(ProxiedPlayer player, ProxiedPlayer target) {lastMessage.put(player.getName(), target.getName());}

    static boolean isAllowingDms(ProxiedPlayer player){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            connection = Main.getConnection();
            ps = connection.prepareStatement("SELECT * FROM fadelands_players_settings WHERE player_uuid = ?");
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if(rs.next()){
                if(rs.getBoolean("private_messages")){
                    return true;
                }else{
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Main.closeComponents(rs, ps, connection);
        }
        return true;
    }
}
