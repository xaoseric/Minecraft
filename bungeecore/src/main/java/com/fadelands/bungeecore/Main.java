package com.fadelands.bungeecore;

import com.fadelands.bungeecore.commands.AlertCommand;
import com.fadelands.bungeecore.commands.ConnectCommand;
import com.fadelands.bungeecore.commands.reports.ReportInfoCommand;
import com.fadelands.bungeecore.commands.reports.ReportsCommand;
import com.fadelands.bungeecore.commands.servers.*;
import com.fadelands.bungeecore.discord.BuildBot;
import com.fadelands.bungeecore.players.ChatLogging;
import com.fadelands.bungeecore.pm.PrivateMessageCommand;
import com.fadelands.bungeecore.utils.Utils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.fadelands.bungeecore.commands.FindCommand;
import com.fadelands.bungeecore.commands.SCCommand;
import com.fadelands.bungeecore.commands.reports.HandleReportCommand;
import com.fadelands.bungeecore.commands.reports.ReportCommand;
import com.fadelands.bungeecore.discord.discordsync.LinkDiscordID;
import com.fadelands.bungeecore.players.BungeeCommandsLogging;
import com.fadelands.bungeecore.pm.ReplyCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.*;
import java.util.function.Consumer;

@SuppressWarnings("ALL")
public class Main extends Plugin {

    private String host;
    private String user;
    private String pass;
    private String db;
    private static HikariConfig hikariConfig;
    private static HikariDataSource hikariDataSource;
    public static Configuration config;
    public static File configfile;

    private Plugin plugin;

    public void onEnable() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        configfile = new File(getDataFolder(), "config.yml");


        if (!configfile.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, configfile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.host = "localhost";
        this.user = "root";
        this.pass = "";
        this.db = "fadelands_server";

        if (this.user == null || this.pass == null) {
            ProxyServer.getInstance().getLogger().warning("WARNING!!! Couldn't find a database connection for the Bungee Proxy.");
            return;
        }

        getProxy().getInstance().getLogger().severe("Loading database connection");

        this.hikariConfig = new HikariConfig();
        this.hikariConfig.setJdbcUrl("jdbc:mysql://" + this.host + ":3306/" + this.db + "?useSSL=false");
        this.hikariConfig.setUsername(this.user);
        this.hikariConfig.setPassword(this.pass);
        this.hikariConfig.setConnectionTimeout(10 * 1000);
        this.hikariConfig.setIdleTimeout(120 * 1000);
        this.hikariConfig.setMaximumPoolSize(7);
        this.hikariConfig.setPoolName("flbungee");

        try {
            this.hikariDataSource = new HikariDataSource(hikariConfig);

        } catch (Exception e) {
            e.printStackTrace();
        }

        BuildBot.build();
        registerCommands();
        registerEvents();

        getProxy().getConsole().sendMessage(new ComponentBuilder(Utils.BungeeCore + "Plugin has been enabled.").color(ChatColor.GREEN).create());

    }
    private void registerEvents() {
        getProxy().getPluginManager().registerListener(this, new BungeeCommandsLogging(this));
        getProxy().getPluginManager().registerListener(this, new ChatLogging(this));
    }
    private void registerCommands() {
        getProxy().getPluginManager().registerCommand(this, new FindCommand());
        getProxy().getPluginManager().registerCommand(this, new AlertCommand());
        getProxy().getPluginManager().registerCommand(this, new SCCommand());

        getProxy().getPluginManager().registerCommand(this, new ReportsCommand());
        getProxy().getPluginManager().registerCommand(this, new ReportCommand(this));
        getProxy().getPluginManager().registerCommand(this, new ReportInfoCommand());
        getProxy().getPluginManager().registerCommand(this, new HandleReportCommand());

        getProxy().getPluginManager().registerCommand(this, new HubCommand());
        getProxy().getPluginManager().registerCommand(this, new SkyBlockCommand());
        getProxy().getPluginManager().registerCommand(this, new SurvivalCommand());
        getProxy().getPluginManager().registerCommand(this, new BuildCommand());
        getProxy().getPluginManager().registerCommand(this, new CurrentServerCommand());
        getProxy().getPluginManager().registerCommand(this, new ConnectCommand(this));

        getProxy().getPluginManager().registerCommand(this, new LinkDiscordID());

        getProxy().getPluginManager().registerCommand(this, new PrivateMessageCommand());
        getProxy().getPluginManager().registerCommand(this, new ReplyCommand());
    }
        public void onDisable () {
            getProxy().getConsole().sendMessage(new ComponentBuilder(Utils.BungeeCore + "Plugin has been disabled.").color(ChatColor.RED).create());

            ProxyServer.getInstance().getPluginManager().unregisterCommands(this);

        }
    public static Connection getConnection() throws SQLException {
        try{
            return hikariDataSource.getConnection();
        } catch (SQLException e){
            try{
                // Trying once more.
                return hikariDataSource.getConnection();
            } catch (SQLTimeoutException e2){
                e2.printStackTrace();
                ProxyServer.getInstance().getLogger().severe("Couldn't grab a database connection. Stopping the server.");
            }
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
    }


