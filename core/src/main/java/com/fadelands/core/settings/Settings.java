package com.fadelands.core.settings;

import com.fadelands.array.Array;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("ALL")
public class Settings {

    public boolean allowingFriendRequests(Player player) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM fadelands_players_settings WHERE player_uuid = ?";
            connection = Array.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("friend_requests");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.closeComponents(rs, ps, connection);
        }
        return true;
    }

    public boolean joinLeaveMessages(Player player) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM fadelands_players_settings WHERE player_uuid = ?";
            connection = Array.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("join_leave_messages");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.closeComponents(rs, ps, connection);
        }
        return true;
    }

    public boolean publicChat(Player player) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM fadelands_players_settings WHERE player_uuid = ?";
            connection = Array.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("public_chat");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.closeComponents(rs, ps, connection);
        }
        return true;
    }

    public boolean allowingPrivateMessages(Player player) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM fadelands_players_settings WHERE player_uuid = ?";
            connection = Array.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("private_messages");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.closeComponents(rs, ps, connection);
        }
        return true;
    }

    public boolean informIfMuted(Player player) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM fadelands_players_settings WHERE player_uuid = ?";
            connection = Array.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("inform_if_muted");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.closeComponents(rs, ps, connection);
        }
        return true;
    }

    public boolean allowingGameTips(Player player){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String query = "SELECT * FROM fadelands_players_settings WHERE player_uuid = ?";
            connection = Array.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if(rs.next()){
                return rs.getBoolean("game_tips");
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            Array.closeComponents(rs, ps, connection);
        }
        return true;
    }

    public boolean showScoreboard(Player player){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String query = "SELECT * FROM fadelands_players_settings WHERE player_uuid = ?";
            connection = Array.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if(rs.next()){
                return rs.getBoolean("show_scoreboard");
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            Array.closeComponents(rs, ps, connection);
        }
        return true;
    }

    public boolean showFriendJoinAlerts(Player player){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String query = "SELECT * FROM fadelands_players_settings WHERE player_uuid = ?";
            connection = Array.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if(rs.next()){
                return rs.getBoolean("show_friend_join_alerts");
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            Array.closeComponents(rs, ps, connection);
        }
        return true;
    }

    public boolean showFriendAlerts(Player player){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String query = "SELECT * FROM fadelands_players_settings WHERE player_uuid = ?";
            connection = Array.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if(rs.next()){
                return rs.getBoolean("show_friend_alerts");
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            Array.closeComponents(rs, ps, connection);
        }
        return true;
    }

    public boolean allowPartyInvites(Player player){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String query = "SELECT * FROM fadelands_players_settings WHERE player_uuid = ?";
            connection = Array.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if(rs.next()){
                return rs.getBoolean("party_requests");
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            Array.closeComponents(rs, ps, connection);
        }
        return true;
    }

    public boolean showAnnouncements(Player player){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String query = "SELECT * FROM fadelands_players_settings WHERE player_uuid = ?";
            connection = Array.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if(rs.next()){
                return rs.getBoolean("show_announcements");
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            Array.closeComponents(rs, ps, connection);
        }
        return true;
    }

    public boolean lobbyDoubleJump(Player player){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String query = "SELECT * FROM fadelands_players_lobbysettings WHERE player_uuid = ?";
            connection = Array.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if(rs.next()){
                return rs.getBoolean("double_jump");
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            Array.closeComponents(rs, ps, connection);
        }
        return true;
    }

    public boolean lobbyPlayerVisibility(Player player){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String query = "SELECT * FROM fadelands_players_lobbysettings WHERE player_uuid = ?";
            connection = Array.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if(rs.next()){
                return rs.getBoolean("player_visibility");
            }
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            Array.closeComponents(rs, ps, connection);
        }
        return true;
    }
}

