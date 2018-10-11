package com.fadelands.array.manager;

import com.fadelands.array.Array;
import com.fadelands.array.database.Tables;
import com.fadelands.array.player.User;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.function.Consumer;

public class DatabaseManager {

    private HikariConfig hikariConfig;
    private HikariDataSource hikariDataSource;

    private String host;
    private String user;
    private String pass;
    private String db;

    public DatabaseManager() {
        this.host = "localhost";
        this.user = "root";
        this.pass = "";
        this.db = "fadelands_server";
    }

    public void init() {
        this.hikariConfig = new HikariConfig();
        this.hikariConfig.setJdbcUrl("jdbc:mysql://" + this.host + ":3306/" + this.db + "?useSSL=false");
        this.hikariConfig.setUsername(this.user);
        this.hikariConfig.setPassword(this.pass);
        this.hikariConfig.setMaximumPoolSize(10);
        this.hikariConfig.setRegisterMbeans(true);
        this.hikariConfig.setPoolName("flarray");

        try {
            this.hikariDataSource = new HikariDataSource(this.hikariConfig);
            Tables.createTables();
        } catch (Exception e) {
            Bukkit.getLogger().warning("Couldn't connect to the database pool. Shutting down.");
            Bukkit.shutdown();
        }
    }

    public void shutdown() {
        if(this.hikariDataSource.isRunning()) {
            this.hikariDataSource.close();
        }
    }

    public Connection getConnection() throws SQLException {
        try {
            return this.hikariDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hikariDataSource.getConnection();
    }

    public ResultSet executeQuery(Connection connection, PreparedStatement stmt) throws SQLException {
        return stmt.executeQuery();
    }

    public int executeUpdate(Connection connection, PreparedStatement stmt) throws SQLException {
        return stmt.executeUpdate();
    }

    public void executeQuery(String query, Consumer<ResultSet> consumer) {
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            connection = this.hikariDataSource.getConnection();
            st = connection.prepareStatement(query);
            rs = st.executeQuery();

            consumer.accept(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (st != null) {
                try {
                    st.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void closeComponents(ResultSet rs, PreparedStatement ps, Connection connection) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void closeComponents(PreparedStatement ps, Connection connection) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeComponents(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeComponents(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeComponents(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateTable(Player player, String table, String columnName, String value) {
        try (Connection connection = getConnection()) {
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

    public void updateTable(Player player, String table, String columnName, Boolean value) {
        try (Connection connection = getConnection()) {
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

    public void updateTable(Player player, String table, String columnName, Integer value) {
        try (Connection connection = getConnection()) {
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

    public void updateTable(Player player, String table, String columnName, Timestamp value) {
        try (Connection connection = getConnection()) {
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

    public void updateTable(String playerName, String table, String columnName, String value) {
        try (Connection connection = getConnection()) {
            String query = "UPDATE " + table + " SET " + columnName + "=? WHERE player_uuid='" + new User().getUuid(playerName) + "'";
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

    public void updateTable(String playerName, String table, String columnName, Boolean value) {
        try (Connection connection = getConnection()) {
            String query = "UPDATE " + table + " SET " + columnName + "=? WHERE player_uuid='" + new User().getUuid(playerName) + "'";
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

    public void updateTable(String playerName, String table, String columnName, Integer value) {
        try (Connection connection = getConnection()) {
            String query = "UPDATE " + table + " SET " + columnName + "=? WHERE player_uuid='" + new User().getUuid(playerName) + "'";
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

    public void updateTable(String playerName, String table, String columnName, Timestamp value) {
        try (Connection connection = getConnection()) {
            String query = "UPDATE " + table + " SET " + columnName + "=? WHERE player_uuid='" + new User().getUuid(playerName) + "'";
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

    public boolean createTable(String query) {
        try (Connection connection = getConnection()) {
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

    public void deleteFromTable(Player player, String table) {
        try (Connection connection = getConnection()) {
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

    public void insertTo(String table, String column, String value) {
        try (Connection connection = getConnection()) {
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

    public void insertTo(String table, String column, Integer value) {
        try (Connection connection = getConnection()) {
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
