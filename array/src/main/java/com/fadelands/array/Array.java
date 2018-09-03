package com.fadelands.array;

import com.fadelands.array.commands.admin.CountryCommandExecutor;
import com.fadelands.array.commands.admin.DatabaseStatusCommandExecutor;
import com.fadelands.array.commands.admin.LockdownCommandExecutor;
import com.fadelands.array.commands.admin.WhoIsCommandExecutor;
import com.fadelands.array.manager.GeoManager;
import com.fadelands.array.manager.ServerManager;
import com.fadelands.array.punishments.PunishmentManager;
import com.fadelands.array.punishments.commands.*;
import com.fadelands.array.punishments.PunishmentMenu;
import com.fadelands.array.utils.PluginMessage;
import com.fadelands.array.database.Tables;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.fadelands.array.commands.admin.UptimeCommandExecutor;
import okhttp3.OkHttpClient;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.*;
import java.util.function.Consumer;
@SuppressWarnings("ALL")
public class Array extends JavaPlugin {

    private long serverStartTime = System.currentTimeMillis();

    private String host;
    private String user;
    private String pass;
    private String db;
    private static FileConfiguration fileConfiguration;
    private static HikariConfig hikariConfig;
    private static HikariDataSource hikariDataSource;
    private OkHttpClient okHttpClient;
    private PluginMessage pluginMessage;
    private PunishmentMenu punishmentMenu;
    private PunishmentManager punishmentManager;
    private GeoManager geoManager;
    private ServerManager serverManager;

    public static Array plugin;


    public void onEnable() {
        plugin = this;
        this.saveDefaultConfig();

        this.host = "localhost";
        this.user = "root";
        this.pass = "";
        this.db = "fadelands_server";

        Bukkit.getLogger().info("[Array] Loading database connection");

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
            e.printStackTrace();
        }
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", pluginMessage = new PluginMessage(this));

        registerEvents();
        registerCommands();

        Bukkit.getLogger().info("[Array] Plugin has been enabled.");
    }

    private void registerCommands() {
        getCommand("uptime").setExecutor(new UptimeCommandExecutor(this));
        getCommand("dbstatus").setExecutor(new DatabaseStatusCommandExecutor(this));
        getCommand("whois").setExecutor(new WhoIsCommandExecutor(this));
        getCommand("punish").setExecutor(new PunishCommandExecutor(this));
        getCommand("ban").setExecutor(new BanCommandExecutor(this, getPunishmentManager()));
        getCommand("mute").setExecutor(new MuteCommandExecutor(this, getPunishmentManager()));
        getCommand("history").setExecutor(new HistoryCommandExecutor(this, getPunishmentManager()));
        getCommand("alts").setExecutor(new AltsCommandExecutor(this));
        getCommand("country").setExecutor(new CountryCommandExecutor(this));
        getCommand("lockdown").setExecutor(new LockdownCommandExecutor(this));

    }

    private void registerEvents(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PunishmentMenu(this), this);
        pm.registerEvents(new PunishmentManager(this), this);
        this.punishmentMenu = new PunishmentMenu(this);
        this.punishmentManager = new PunishmentManager(this);
        this.okHttpClient = new OkHttpClient();
        this.geoManager = new GeoManager(this);
        this.serverManager = new ServerManager(this);
        pm.registerEvents(new ServerManager(this), this);


    }

    public void onDisable() {

        Bukkit.getLogger().info("[Array] Plugin has been disabled.");
    }

    public static Connection getConnection() throws SQLException {
        try {
            return hikariDataSource.getConnection();
        } catch (SQLException e) {
                e.printStackTrace();
                Bukkit.getLogger().severe("Warning! Couldn't find a database connection for Array. Plugins won't function without a db connection.");
            }
        return hikariDataSource.getConnection();
    }

    public static ResultSet executeQuery(Connection connection, PreparedStatement stmt) throws SQLException {
        return stmt.executeQuery();
    }

    public static int executeUpdate(Connection connection, PreparedStatement stmt) throws SQLException {
        return stmt.executeUpdate();
    }

    public static void executeQuery(String query, Consumer<ResultSet> consumer) {
        Connection connection = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            connection = hikariDataSource.getConnection();
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

    public static void closeComponents(ResultSet rs, PreparedStatement ps, Connection connection) {
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

    public static void closeComponents(PreparedStatement ps, Connection connection) {
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

    public static void closeComponents(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeComponents(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeComponents(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static String getUsernameFromUUID(String uuid) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT player_username FROM fadelands_players WHERE uuid = ?;";

        try {
            connection = Array.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, uuid);

            rs = Array.executeQuery(connection, ps);
            if (rs.next()) {
                return rs.getString("player_username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.closeComponents(rs, ps, connection);
        }

        return null;
    }

    public static String getUuidFromUsername(String username) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "SELECT player_uuid FROM fadelands_players WHERE player_username = ?;";

        try {
            connection = Array.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, username);

            rs = Array.executeQuery(connection, ps);
            if (rs.next()) {
                return rs.getString("player_uuid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Array.closeComponents(rs, ps, connection);
        }

        return null;
    }

    public long getServerStartTime() {
        return this.serverStartTime;
    }
    public long getServerUptime() {
        return System.currentTimeMillis() - this.serverStartTime;
    }

    public PluginMessage getPluginMessage() {
        return pluginMessage;
    }

    public PunishmentMenu getPunishmentMenu() {
        return punishmentMenu;
    }

    public PunishmentManager getPunishmentManager() {
        return punishmentManager;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public GeoManager getGeoManager() {
        return geoManager;
    }

    public ServerManager getServerManager() {
        return serverManager;
    }
}
