package com.fadelands.array.utils;

import com.fadelands.array.Array;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServerStatistics {

    public int getUniquePlayers() {
        try (Connection connection = Array.plugin.getDatabaseManager().getConnection()) {
            try (PreparedStatement st = connection.prepareStatement("SELECT COUNT(*) FROM players")) {
                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getAmountChatMessages() {
        try (Connection connection = Array.plugin.getDatabaseManager().getConnection()) {
            try (PreparedStatement st = connection.prepareStatement("SELECT COUNT(*) FROM chat_messages")) {
                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getCommandsUsed() {
        try (Connection connection = Array.plugin.getDatabaseManager().getConnection()) {
            try (PreparedStatement st = connection.prepareStatement("SELECT COUNT(*) FROM chat_commands")) {
                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getIssuedPunishments() {
        try (Connection connection = Array.plugin.getDatabaseManager().getConnection()) {
            try (PreparedStatement st = connection.prepareStatement("SELECT COUNT(*) FROM punishments")) {
                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
