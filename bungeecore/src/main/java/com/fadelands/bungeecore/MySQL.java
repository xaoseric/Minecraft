package com.fadelands.bungeecore;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

@SuppressWarnings("ALL")
public class MySQL {

    public static void updateTable(ProxiedPlayer player, String table, String columnName, String value) {
        try (Connection connection = Main.getConnection()) {
            String query = "UPDATE " + table + " SET " + columnName + "=? WHERE player_uuid='" + player.getUniqueId().toString() + "'";
            try (PreparedStatement p = connection.prepareStatement(query)) {

                p.setString(1, value);
                p.executeUpdate();
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateTable(ProxiedPlayer player, String table, String columnName, Integer value) {
        try (Connection connection = Main.getConnection()) {
            String query = "UPDATE " + table + " SET " + columnName + "=? WHERE player_uuid='" + player.getUniqueId().toString() + "'";
            try (PreparedStatement p = connection.prepareStatement(query)) {

                p.setInt(1, value);
                p.executeUpdate();
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateTable(ProxiedPlayer player, String table, String columnName, Boolean value) {
        try (Connection connection = Main.getConnection()) {
            String query = "UPDATE " + table + " SET " + columnName + "=? WHERE player_uuid='" + player.getUniqueId().toString() + "'";
            try (PreparedStatement p = connection.prepareStatement(query)) {

                p.setBoolean(1, value);
                p.executeUpdate();
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateTable(ProxiedPlayer player, String table, String columnName, Timestamp value) {
        try (Connection connection = Main.getConnection()) {
            String query = "UPDATE " + table + " SET " + columnName + "=? WHERE player_uuid='" + player.getUniqueId().toString() + "'";
            try (PreparedStatement p = connection.prepareStatement(query)) {

                p.setTimestamp(1, value);
                p.executeUpdate();
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean createTable(String query) {
        try (Connection connection = Main.getConnection()) {
            try (PreparedStatement p = connection.prepareStatement(query)) {
                p.executeUpdate(query);
            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void deleteFromTable(ProxiedPlayer player, String table) {
        try (Connection connection = Main.getConnection()) {
            String query = ("DELETE FROM " + table + " WHERE player_uuid='" + player.getUniqueId().toString() + "'");
            try (PreparedStatement p = connection.prepareStatement(query)) {
                p.executeUpdate();

            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void insertTo(String table, String column, String value) {
        try (Connection connection = Main.getConnection()) {
            String query = ("INSERT INTO " + table + " (" + column + ") VALUE (?)");
            try (PreparedStatement p = connection.prepareStatement(query)) {
                p.setString(1, value);
                p.executeUpdate();

            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void insertTo(String table, String column, Integer value) {
        try (Connection connection = Main.getConnection()) {
            String query = ("INSERT INTO " + table + " (" + column + ") VALUE (?)");
            try (PreparedStatement p = connection.prepareStatement(query)) {
                p.setInt(1, value);
                p.executeUpdate();

            } finally {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
