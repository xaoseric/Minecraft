package com.fadelands.core.economy;

import com.fadelands.core.Core;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.fadelands.core.utils.UtilNumber.isBetween;

public class EconomyManager {

    private Core plugin;

    public EconomyManager(Core plugin) {
        this.plugin = plugin;
    }

    public int getPoints(String uuid) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM stats_global WHERE player_uuid = ?";

        try {
            connection = plugin.getDatabaseManager().getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, uuid);
            rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getInt("points");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            plugin.getDatabaseManager().closeComponents(rs,ps,connection);
        }
        return 0;
    }

    @SuppressWarnings("Duplicates")
    public void setPoints(String uuid, int points) {
        Connection connection = null;
        PreparedStatement ps = null;

        String query = "UPDATE stats_global SET points=? WHERE player_uuid=?";

        try {
            connection = plugin.getDatabaseManager().getConnection();
            ps = connection.prepareStatement(query);
            ps.setInt(1, points);
            ps.setString(1, uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            plugin.getDatabaseManager().closeComponents(ps,connection);
        }
    }

    public int getTokens(String uuid) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM stats_global WHERE player_uuid = ?";

        try {
            connection = plugin.getDatabaseManager().getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, uuid);
            rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getInt("tokens");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            plugin.getDatabaseManager().closeComponents(rs,ps,connection);
        }
        return 0;
    }

    @SuppressWarnings("Duplicates")
    public void setTokens(String uuid, int tokens) {
        Connection connection = null;
        PreparedStatement ps = null;

        String query = "UPDATE stats_global SET tokens=? WHERE player_uuid=?";

        try {
            connection = plugin.getDatabaseManager().getConnection();
            ps = connection.prepareStatement(query);
            ps.setInt(1, tokens);
            ps.setString(1, uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            plugin.getDatabaseManager().closeComponents(ps,connection);
        }
    }

    public String getNetworkLevel(String uuid) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM stats_global WHERE player_uuid = ?";

        try {
            connection = plugin.getDatabaseManager().getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, uuid);
            rs = ps.executeQuery();
            if(rs.next()) {
                int level = rs.getInt("network_level");
                if(isBetween(0, level, 9)) {
                    return ChatColor.GRAY + "" + level;
                }
                if(isBetween(10, level, 19)) {
                    return ChatColor.YELLOW + "" + level;
                }
                if(isBetween(20, level, 29)) {
                    return ChatColor.GOLD + "" + level;
                }
                if(isBetween(30, level, 39)) {
                    return ChatColor.BLUE + "" + level;
                }
                if(isBetween(40, level, 49)) {
                    return ChatColor.DARK_AQUA + "" + level;
                }
                if(isBetween(50, level, 59)) {
                    return ChatColor.DARK_GREEN + "" + level;
                }
                if(isBetween(60, level, 79)) {
                    return ChatColor.DARK_PURPLE + "" + level;
                }
                if(isBetween(80, level, 99)) {
                    return ChatColor.RED + "" + level;
                }
                if(level == 100) {
                    return ChatColor.DARK_RED + "" + level;
                }
             }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            plugin.getDatabaseManager().closeComponents(rs,ps,connection);
        }
        return "0";
    }
}
