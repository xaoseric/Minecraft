package com.fadelands.array.player;

import com.fadelands.array.Array;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@SuppressWarnings("Duplicates")
public class User {

    public User() {

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
            return rs.next();
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

    public String getLastServer(String player) {
        String server;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            connection = Array.getConnection();
            ps = connection.prepareStatement("SELECT * FROM fadelands_players WHERE player_uuid = ?");
            ps.setString(1, new User().getUuid(player));
            rs = ps.executeQuery();
            if(rs.next()) {
                server = rs.getString("last_server");
                return server;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.closeComponents(rs, ps, connection);
        }
        return "Unknown";
    }

    public boolean isRedTag(String name) {
        String rank = Objects.requireNonNull(Array.plugin.getLuckPermsApi().getUser(name)).getPrimaryGroup();
        return rank.equals("owner") || (rank.equals("admin") || (rank.equals("developer")));
    }

    public boolean isAdmin(String name) {
        String rank = Objects.requireNonNull(Array.plugin.getLuckPermsApi().getUser(name)).getPrimaryGroup();
        return rank.equals("owner") || (rank.equals("admin"));
    }

    public boolean isSenior(String name) {
        String rank = Objects.requireNonNull(Array.plugin.getLuckPermsApi().getUser(name)).getPrimaryGroup();
        return rank.equals("senior") || (rank.equals("owner") || (rank.equals("admin")));
    }

    public boolean isMod(String name) {
        String rank = Objects.requireNonNull(Array.plugin.getLuckPermsApi().getUser(name)).getPrimaryGroup();
        return rank.equals("mod") || (rank.equals("senior") || (rank.equals("owner") || (rank.equals("admin"))));
    }

    public boolean isTrainee(String name) {
        String rank = Objects.requireNonNull(Array.plugin.getLuckPermsApi().getUser(name)).getPrimaryGroup();
        return rank.equals("trainee") || (rank.equals("mod") || (rank.equals("senior") || (rank.equals("owner") || (rank.equals("admin")))));
    }

    public boolean isBuilder(String name) {
        String rank = Objects.requireNonNull(Array.plugin.getLuckPermsApi().getUser(name)).getPrimaryGroup();
        return rank.equals("builder") || rank.equals("admin") || (rank.equals("owner"));
    }

    public boolean isStaff(String name) {
        String rank = Objects.requireNonNull(Array.plugin.getLuckPermsApi().getUser(name)).getPrimaryGroup();
        return rank.equals("trainee") || (rank.equals("mod") || (rank.equals("senior") || (rank.equals("developer") || rank.equals("admin") || (rank.equals("owner")))));
    }

    public boolean isDonatorRank(String name) {
        String rank = Objects.requireNonNull(Array.plugin.getLuckPermsApi().getUser(name)).getPrimaryGroup();
        return rank.equals("donator") || (rank.equals("premium") || (rank.equals("platinum") || (rank.equals("contributor"))));
    }

    public Player getOnlineAdmins() {
        for (Player admins : Bukkit.getOnlinePlayers()) {
            if (isAdmin(admins.getName())) {
                return admins;
            }
        }
        return null;
    }

    public String getRank(String playerName) {
        //noinspection ConstantConditions
        return Array.plugin.getLuckPermsApi().getUserManager().getUser(playerName).getPrimaryGroup();
    }
}
