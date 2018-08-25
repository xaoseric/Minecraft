package com.fadelands.array.players;

import com.fadelands.array.Array;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("Duplicates")
public class FadeLandsUser {

    public FadeLandsUser() {

    }

    public String getName(String username) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Array.getConnection();
            ps = connection.prepareStatement("SELECT * FROM fadelands_players WHERE player_username = ?");
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("player_username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.closeComponents(rs, ps, connection);
        }
        return "N/A";
    }

    public String getUuid(String username) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Array.getConnection();
            ps = connection.prepareStatement("SELECT * FROM fadelands_players WHERE player_username = ?");
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("player_uuid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.closeComponents(rs, ps, connection);
        }
        return "N/A";
    }

    public String getNameFromUuid(String UUID) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Array.getConnection();
            ps = connection.prepareStatement("SELECT * FROM fadelands_players WHERE player_uuid = ?");
            ps.setString(1, UUID);
            rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getString("player_username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.closeComponents(rs, ps, connection);
        }
        return "N/A";
    }

    public String getIp(String username) {
        String ip;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            connection = Array.getConnection();
            ps = connection.prepareStatement("SELECT * FROM fadelands_players WHERE player_username = ?");
            ps.setString(1, username);
            rs = ps.executeQuery();
            if(rs.next()) {
                if (rs.getString("last_ip") == null) {
                    ip = rs.getString("first_ip");
                } else {
                    ip = rs.getString("last_ip");
                }
                return ip;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.closeComponents(rs, ps, connection);
        }
        return "N/A";
    }

    public String getIpFromUuid(String uuid) {
        String ip;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            connection = Array.getConnection();
            ps = connection.prepareStatement("SELECT * FROM fadelands_players WHERE player_uuid = ?");
            ps.setString(1, uuid);
            rs = ps.executeQuery();
            if(rs.next()) {
                if (rs.getString("last_ip") == null) {
                    ip = rs.getString("first_ip");
                } else {
                    ip = rs.getString("last_ip");
                }
                return ip;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.closeComponents(rs, ps, connection);
        }
        return "N/A";
    }

    public boolean hasPlayedBefore(String name){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            connection = Array.getConnection();
            ps = connection.prepareStatement("SELECT * FROM fadelands_players WHERE player_username = ?");
            ps.setString(1, name);
            rs = ps.executeQuery();
            if(!rs.next()){
                return false;
            }else{
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.closeComponents(rs, ps, connection);
        }
        return false;

    }

    public long firstJoined(String name){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            connection = Array.getConnection();
            ps = connection.prepareStatement("SELECT * FROM fadelands_players WHERE player_username = ?");
            ps.setString(1, name);
            rs = ps.executeQuery();
            if(!rs.next()){
                return 0;
            }else{
                return rs.getTimestamp("first_join").getTime();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.closeComponents(rs, ps, connection);
        }
        return 0;
    }

    public long lastLogin(String name){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            connection = Array.getConnection();
            ps = connection.prepareStatement("SELECT * FROM fadelands_players WHERE player_username = ?");
            ps.setString(1, name);
            rs = ps.executeQuery();
            if(!rs.next()){
                return 0;
            }else{
                return rs.getTimestamp("last_login").getTime();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.closeComponents(rs, ps, connection);
        }
        return 0;
    }
}
