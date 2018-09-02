package com.fadelands.array.manager;

import com.fadelands.array.Array;
import com.fadelands.array.players.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Stream;

public class ServerManager implements Listener {

    private Array array;

    public ServerManager(Array array) {
        this.array = array;
    }

    public boolean lockdownActive() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = Array.getConnection();
            ps = connection.prepareStatement("SELECT * FROM fadelands_server_lockdown WHERE active = ?");
            ps.setBoolean(1, true);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.closeComponents(rs, ps, connection);
        }
        return true;
    }

    public String getLockdownReason() {
        if (!lockdownActive()) {
            return "No current lockdown.";
        } else {
            Connection connection = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                connection = Array.getConnection();
                ps = connection.prepareStatement("SELECT * FROM fadelands_server_lockdown WHERE active = true");
                rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getString("reason");
                } else {
                    return "Not Available";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                Array.closeComponents(rs, ps, connection);
            }
        }
        return "Not Available";
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerLogin(AsyncPlayerPreLoginEvent event) {
        if (lockdownActive()) {
            User user = new User();
            if(user.isRedTag(event.getName())) {
                event.allow();
                return;
            }
                System.out.println("a randy loggin in :v");
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, "§6§lThe server is currently on lockdown.\n§f" + getLockdownReason() + "\n\n§2Look out for news on our website, Discord and Twitter.");
            }
    }

    public void activateLockdown(Player lockdowner, String reason) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = Array.getConnection();
            ps = connection.prepareStatement("UPDATE fadelands_server_lockdown SET lockdowner = ?, active = ?, reason = ? WHERE active = false");
            ps.setString(1, lockdowner.getUniqueId().toString());
            ps.setBoolean(2, true);
            ps.setString(3, reason);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.closeComponents(ps, connection);
        }
        array.getPluginMessage().getPlayerNames("ALL");
    }


    public void disableLockdown() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = Array.getConnection();
            ps = connection.prepareStatement("UPDATE fadelands_server_lockdown SET active = ? WHERE active = true");
            ps.setBoolean(1, false);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.closeComponents(ps, connection);
        }
    }
}
