package com.fadelands.array;

import com.fadelands.array.commands.admin.*;
import com.fadelands.array.commands.admin.inventory.WhoisInventory;
import com.fadelands.array.manager.GeoManager;
import com.fadelands.array.manager.PlayerManager;
import com.fadelands.array.manager.ServerManager;
import com.fadelands.array.punishments.PunishmentManager;
import com.fadelands.array.punishments.commands.*;
import com.fadelands.array.punishments.PunishmentMenu;
import com.fadelands.array.staff.StaffMode;
import com.fadelands.array.staff.StaffSettings;
import com.fadelands.array.staff.command.StaffManagementCommand;
import com.fadelands.array.staff.command.StaffSettingsCommand;
import com.fadelands.array.staff.command.VanishCommand;
import com.fadelands.array.staff.inventory.SettingsInventory;
import com.fadelands.array.staff.inventory.StaffInventory;
import com.fadelands.array.utils.PluginMessage;
import com.fadelands.array.database.Tables;
import com.fadelands.array.utils.ServerStatistics;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.lucko.luckperms.api.LuckPermsApi;
import okhttp3.OkHttpClient;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
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
    private PlayerManager playerManager;
    private ServerStatistics serverStats;
    private StaffSettings staffSettings;

    private LuckPermsApi luckPerms;
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

        RegisteredServiceProvider<LuckPermsApi> provider = Bukkit.getServicesManager().getRegistration(LuckPermsApi.class);
        if (provider == null) {
            Bukkit.getLogger().warning("[Array] Something went wrong when attempting to load LuckPerms API.");
        } else {
            luckPerms = provider.getProvider();
            Bukkit.getLogger().warning("[Array] Loaded LuckPerms API.");
        }

        registerEvents();
        registerCommands();

        Bukkit.getLogger().info("[Array] Plugin has been enabled.");
    }

    private void registerCommands() {
        getCommand("uptime").setExecutor(new UptimeCommand(this));
        getCommand("dbstatus").setExecutor(new DatabaseStatusCommand(this));
        getCommand("whois").setExecutor(new WhoIsCommand(this));
        getCommand("punish").setExecutor(new PunishCommand(this));
        getCommand("ban").setExecutor(new BanCommand(this, getPunishmentManager()));
        getCommand("mute").setExecutor(new MuteCommand(this, getPunishmentManager()));
        getCommand("history").setExecutor(new HistoryCommand(this, getPunishmentManager()));
        getCommand("alts").setExecutor(new AltsCommand(this));
        getCommand("country").setExecutor(new CountryCommand(this));
        getCommand("lockdown").setExecutor(new LockdownCommand(this));
        getCommand("staffsettings").setExecutor(new StaffSettingsCommand(this));
        getCommand("staff").setExecutor(new StaffManagementCommand(this));
        getCommand("vanish").setExecutor(new VanishCommand(this, new StaffMode(getStaffSettings()), getStaffSettings()));
    }

    private void registerEvents(){
        PluginManager pm = Bukkit.getPluginManager();
        this.punishmentMenu = new PunishmentMenu(this);
        this.punishmentManager = new PunishmentManager(this);
        pm.registerEvents(new PunishmentManager(this), this);
        this.okHttpClient = new OkHttpClient();
        this.geoManager = new GeoManager(this);
        this.serverManager = new ServerManager(this);
        pm.registerEvents(new ServerManager(this), this);
        this.playerManager = new PlayerManager(this);
        pm.registerEvents(new PlayerManager(this), this);
        this.serverStats = new ServerStatistics();
        this.staffSettings = new StaffSettings();

        pm.registerEvents(new PunishmentMenu(this), this);
        pm.registerEvents(new WhoisInventory(this), this);
        pm.registerEvents(new SettingsInventory(this), this);
        pm.registerEvents(new WhoisInventory(this), this);
        pm.registerEvents(new StaffInventory(this), this);
        pm.registerEvents(new StaffMode(getStaffSettings()), this);
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

    public Connection getDatabaseConnection() throws SQLException {
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

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public LuckPermsApi getLuckPermsApi() {
        return luckPerms;
    }

    public ServerStatistics getServerStats() {
        return serverStats;
    }

    public StaffSettings getStaffSettings() {
        return staffSettings;
    }

}
