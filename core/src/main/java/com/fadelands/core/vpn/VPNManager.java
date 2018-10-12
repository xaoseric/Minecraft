package com.fadelands.core.vpn;

import com.fadelands.core.Core;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VPNManager implements Listener {

    public List<String> ips = new ArrayList<>();

    private static String REGEX =
            "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    public boolean validIpAddress(String ip) {
        try{
            if(ip == null || ip.isEmpty()) {
                return false;
            }
            String[] parts = ip.split("\\.");
            if(parts.length != 4) {
                return false;
            }
            for(String s : parts) {
                int i = Integer.parseInt(s);
                if((i < 0) || (i > 255)){
                    return false;
                }
            }
            return !ip.endsWith(".");
        } catch(NumberFormatException e) {
            return false;
        }
    }

    @SuppressWarnings("Duplicates")
    public void blockIp(String ip) {
        if(!(validIpAddress(ip))) return;

        Connection connection = null;
        PreparedStatement ps = null;

        String query = "INSERT INTO blocked_ips (ips) VALUES (?)";

        try {
            connection = Core.plugin.getDatabaseManager().getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, ip);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Core.plugin.getDatabaseManager().closeComponents(ps, connection);
        }
    }

    @SuppressWarnings("Duplicates")
    public void unblockIp(String ip) {
        if(!(validIpAddress(ip))) return;

        Connection connection = null;
        PreparedStatement ps = null;

        String query = "DELETE FROM blocked_ips WHERE ips = ?";

        try {
            connection = Core.plugin.getDatabaseManager().getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, ip);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Core.plugin.getDatabaseManager().closeComponents(ps, connection);
        }
    }

    public boolean ipBlocked(String ip) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT * FROM blocked_ips WHERE ips = ?";

        try {
            connection = Core.plugin.getDatabaseManager().getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, ip);
            rs = ps.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Core.plugin.getDatabaseManager().closeComponents(rs, ps, connection);
        }
        return false;
    }

    public void addIps() {
    }
}
