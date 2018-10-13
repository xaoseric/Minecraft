package com.fadelands.core;

import com.fadelands.core.achievements.AchievementManager;
import com.fadelands.core.achievements.command.AchievementCommand;
import com.fadelands.core.commands.*;
import com.fadelands.core.commands.help.command.GuidesCommandExecutor;
import com.fadelands.core.commands.help.command.HelpCommandExecutor;
import com.fadelands.core.commands.help.guides.DiscordLinkGuide;
import com.fadelands.core.commands.help.guides.GuideMenu;
import com.fadelands.core.commands.help.inventory.ApplyGui;
import com.fadelands.core.commands.help.inventory.HelpInventory;
import com.fadelands.core.commands.help.inventory.ServerStatsInventory;
import com.fadelands.core.events.Events;
import com.fadelands.core.database.DatabaseManager;
import com.fadelands.core.manager.GeoManager;
import com.fadelands.core.player.PlayerManager;
import com.fadelands.core.manager.ServerManager;
import com.fadelands.core.monitor.PerformanceManager;
import com.fadelands.core.monitor.command.MonitorCommand;
import com.fadelands.core.npc.NPCManager;
import com.fadelands.core.playerdata.DataEvents;
import com.fadelands.core.profile.command.ProfileCommand;
import com.fadelands.core.profile.inventory.ProfileInventory;
import com.fadelands.core.provider.chat.SimpleChatProvider;
import com.fadelands.core.provider.chat.announcements.AnnouncementListener;
import com.fadelands.core.provider.chat.announcements.Announcements;
import com.fadelands.core.provider.chat.command.ChatSlowCommandExecutor;
import com.fadelands.core.provider.chat.command.SilenceChatCommandExecutor;
import com.fadelands.core.provider.chat.provider.ChatProvider;
import com.fadelands.core.provider.scoreboard.SimpleBoardProvider;
import com.fadelands.core.provider.scoreboard.SimpleboardManager;
import com.fadelands.core.provider.tablist.TablistText;
import com.fadelands.core.punishments.PunishmentManager;
import com.fadelands.core.punishments.commands.*;
import com.fadelands.core.punishments.PunishmentMenu;
import com.fadelands.core.settings.Settings;
import com.fadelands.core.settings.SettingsCommandExecutor;
import com.fadelands.core.staff.StaffMode;
import com.fadelands.core.staff.StaffSettings;
import com.fadelands.core.staff.command.StaffCommand;
import com.fadelands.core.staff.command.StaffSettingsCommand;
import com.fadelands.core.staff.command.VanishCommand;
import com.fadelands.core.staff.inventory.SettingsInventory;
import com.fadelands.core.staff.inventory.StaffInventory;
import com.fadelands.core.utils.PluginMessage;
import com.fadelands.core.utils.ServerStatistics;
import com.fadelands.core.utils.Utils;
import com.fadelands.core.vpn.VPNManager;
import com.fadelands.core.vpn.command.VPNCommand;
import me.lucko.luckperms.api.LuckPermsApi;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import okhttp3.OkHttpClient;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("ALL")
public class Core extends JavaPlugin {

    public long serverStartTime = System.currentTimeMillis();

    private OkHttpClient okHttpClient;
    private PluginMessage pluginMessage;
    private PunishmentMenu punishmentMenu;
    private PunishmentManager punishmentManager;
    private GeoManager geoManager;
    private ServerManager serverManager;
    private PlayerManager playerManager;
    private DatabaseManager databaseManager;
    private NPCManager npcManager;
    private PerformanceManager performanceManager;
    private VPNManager vpnManager;
    private AchievementManager achievementManager;
    private ServerStatistics serverStats;
    private StaffSettings staffSettings;
    private LuckPermsApi luckPerms;
    private Settings settings;
    private ChatProvider chatProvider;
    private com.fadelands.core.provider.chat.Chat serverChat;
    private SimpleboardManager simpleboardManager;
    private Announcements announcements;

    private Economy econ = null;
    private Permission perms = null;
    private Chat chat = null;

    public static Core plugin;

    public void onEnable() {
        plugin = this;
        this.saveDefaultConfig();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", pluginMessage = new PluginMessage(this));

        initVault();
        initLuckPerms();

        registerEvents();
        registerCommands();

        getDatabaseManager().init();

        Bukkit.getLogger().info("[Core] Plugin has been enabled.");
    }

    private void registerCommands() {
        getCommand("uptime").setExecutor(new UptimeCommand(this));
        getCommand("dbstatus").setExecutor(new DatabaseStatusCommand(this));
        getCommand("punish").setExecutor(new PunishCommand(this));
        getCommand("ban").setExecutor(new BanCommand(this, getPunishmentManager()));
        getCommand("mute").setExecutor(new MuteCommand(this, getPunishmentManager()));
        getCommand("history").setExecutor(new HistoryCommand(this, getPunishmentManager()));
        getCommand("alts").setExecutor(new AltsCommand(this));
        getCommand("lockdown").setExecutor(new LockdownCommand(this));
        getCommand("staffsettings").setExecutor(new StaffSettingsCommand(this));
        getCommand("staff").setExecutor(new StaffCommand(this));
        getCommand("vanish").setExecutor(new VanishCommand(this, new StaffMode(getStaffSettings()), getStaffSettings()));
        getCommand("profile").setExecutor(new ProfileCommand(this));
        getCommand("achievements").setExecutor(new AchievementCommand(this));
        getCommand("chatslow").setExecutor(new ChatSlowCommandExecutor(this));
        getCommand("silencechat").setExecutor(new SilenceChatCommandExecutor(this));
        getCommand("help").setExecutor(new HelpCommandExecutor(this));
        getCommand("guides").setExecutor(new GuidesCommandExecutor(this));
        getCommand("settings").setExecutor(new SettingsCommandExecutor(this));
        getCommand("list").setExecutor(new ListCommand(this));
        getCommand("monitor").setExecutor(new MonitorCommand(this));
        getCommand("vpn").setExecutor(new VPNCommand(this));
    }

    private void registerEvents(){
        this.chatProvider = new SimpleChatProvider(this);
        this.simpleboardManager = new SimpleboardManager(this, new SimpleBoardProvider());
        simpleboardManager.runTaskTimerAsynchronously(this, 2L, 2L);
        this.punishmentMenu = new PunishmentMenu(this);
        this.punishmentManager = new PunishmentManager(this);
        this.okHttpClient = new OkHttpClient();
        this.geoManager = new GeoManager(this);
        this.serverManager = new ServerManager(this);
        this.playerManager = new PlayerManager(this);
        this.databaseManager = new DatabaseManager();
        this.performanceManager = new PerformanceManager();
        this.npcManager = new NPCManager();
        this.vpnManager = new VPNManager();
        this.achievementManager = new AchievementManager();
        this.serverStats = new ServerStatistics();
        this.staffSettings = new StaffSettings();
        this.settings = new Settings();
        this.serverChat = new com.fadelands.core.provider.chat.Chat(this);
        this.announcements = new Announcements(this, getSettings(), Core.plugin.getPluginMessage());

        PluginManager pm = Bukkit.getPluginManager(); //can not be placed above

        pm.registerEvents(new PunishmentManager(this), this);
        pm.registerEvents(new ServerManager(this), this);
        pm.registerEvents(new PlayerManager(this), this);
        pm.registerEvents(new PunishmentMenu(this), this);
        pm.registerEvents(new SettingsInventory(this), this);
        pm.registerEvents(new StaffInventory(this), this);
        pm.registerEvents(new StaffMode(getStaffSettings()), this);
        pm.registerEvents(new ProfileInventory(this), this);
        pm.registerEvents(new com.fadelands.core.settings.inventory.SettingsInventory(this), this);
        pm.registerEvents(new Events(this), this);
        pm.registerEvents(new HelpInventory(this), this);
        pm.registerEvents(new ApplyGui(this), this);
        pm.registerEvents(new ServerStatsInventory(this, Core.plugin.getServerStats()), this);
        pm.registerEvents(new GuideMenu(this), this);
        pm.registerEvents(new DiscordLinkGuide(this), this);
        pm.registerEvents(new TablistText(this), this);
        pm.registerEvents(new com.fadelands.core.provider.chat.Chat(this), this);
        pm.registerEvents(getSimpleboardManager(), this);
        pm.registerEvents(getVpnManager(), this);
        pm.registerEvents(new AnnouncementListener(this), this);
        pm.registerEvents(new DataEvents(this), this); // <- PlayerData Events
        pm.registerEvents(new CommandProcess(this), this);
    }

    private void initVault() {
        if (!setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + Utils.Core + "Disabled due to no Vault dependency found! - " + getDescription().getName());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        setupChat();
        setupPermissions();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + Utils.Core + "Vault API hooked into the plugin.");
    }

    private void initLuckPerms() {
        RegisteredServiceProvider<LuckPermsApi> provider = Bukkit.getServicesManager().getRegistration(LuckPermsApi.class);
        if (provider == null) {
            Bukkit.getLogger().warning("[Core] Something went wrong when attempting to load LuckPerms API.");
        } else {
            luckPerms = provider.getProvider();
            Bukkit.getLogger().warning("[Core] Loaded LuckPerms API.");
        }
    }

    public void onDisable() {
        getDatabaseManager().shutdown();
        Bukkit.getLogger().info("[Core] Plugin has been disabled.");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    public long getServerStartTime() {
        return this.serverStartTime;
    }

    public long getServerUptime() {
        return System.currentTimeMillis() - this.serverStartTime;
    }

    public Economy getEconomy() {
        return econ;
    }

    public Permission getPermissions() {
        return perms;
    }

    public Chat getChat() {
        return chat;
    }

    public PluginMessage getPluginMessage() {
        return pluginMessage;
    }

    public SimpleboardManager getSimpleboardManager() {
        return simpleboardManager;
    }

    public ChatProvider getChatProvider() {
        return this.chatProvider;
    }

    public void setChatProvider(ChatProvider chatProvider) {
        this.chatProvider = chatProvider;
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

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public NPCManager getNpcManager() {
        return npcManager;
    }

    public PerformanceManager getPerformanceManager() {
        return performanceManager;
    }

    public VPNManager getVpnManager() {
        return vpnManager;
    }

    public AchievementManager getAchManager() {
        return achievementManager;
    }

    public com.fadelands.core.provider.chat.Chat getServerChat() {
        return this.serverChat;
    }

    public Announcements getAnnouncements() {
        return announcements;
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

    public Settings getSettings() {
        return settings;
    }
}
