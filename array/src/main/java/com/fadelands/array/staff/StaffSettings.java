package com.fadelands.array.staff;

import com.fadelands.array.Array;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StaffSettings {

    public boolean flightOn(Player player) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM staff_settings WHERE player_uuid = ?";
            connection = Array.plugin.getDatabaseManager().getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("flight_toggled");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.plugin.getDatabaseManager().closeComponents(rs, ps, connection);
        }
        return false;
    }

    public boolean vanishOn(Player player) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM staff_settings WHERE player_uuid = ?";
            connection = Array.plugin.getDatabaseManager().getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("vanish_toggled");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.plugin.getDatabaseManager().closeComponents(rs, ps, connection);
        }
        return false;
    }

    public boolean staffChatOn(Player player) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM staff_settings WHERE player_uuid = ?";
            connection = Array.plugin.getDatabaseManager().getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("staff_chat");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.plugin.getDatabaseManager().closeComponents(rs, ps, connection);
        }
        return false;
    }

    public boolean adminNotificationsOn(Player player) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "SELECT * FROM staff_settings WHERE player_uuid = ?";
            connection = Array.plugin.getDatabaseManager().getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, player.getUniqueId().toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("admin_notifications");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.plugin.getDatabaseManager().closeComponents(rs, ps, connection);
        }
        return false;
    }
}
