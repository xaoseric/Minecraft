package com.fadelands.array;

import com.fadelands.array.achievements.AchievementCommand;
import com.fadelands.array.commands.*;
import com.fadelands.array.commands.help.command.GuidesCommandExecutor;
import com.fadelands.array.commands.help.command.HelpCommandExecutor;
import com.fadelands.array.commands.help.guides.DiscordLinkGuide;
import com.fadelands.array.commands.help.guides.GuideMenu;
import com.fadelands.array.commands.help.inventory.ApplyGui;
import com.fadelands.array.commands.help.inventory.HelpInventory;
import com.fadelands.array.commands.help.inventory.ServerStatsInventory;
import com.fadelands.array.events.Events;
import com.fadelands.array.manager.DatabaseManager;
import com.fadelands.array.manager.GeoManager;
import com.fadelands.array.manager.PlayerManager;
import com.fadelands.array.manager.ServerManager;
import com.fadelands.array.npc.NPCManager;
import com.fadelands.array.profile.command.ProfileCommand;
import com.fadelands.array.profile.inventory.ProfileInventory;
import com.fadelands.array.provider.chat.SimpleChatProvider;
import com.fadelands.array.provider.chat.announcements.AnnouncementListener;
import com.fadelands.array.provider.chat.announcements.Announcements;
import com.fadelands.array.provider.chat.command.ChatSlowCommandExecutor;
import com.fadelands.array.provider.chat.command.SilenceChatCommandExecutor;
import com.fadelands.array.provider.chat.provider.ChatProvider;
import com.fadelands.array.provider.scoreboard.SimpleBoardProvider;
import com.fadelands.array.provider.scoreboard.SimpleboardManager;
import com.fadelands.array.provider.tablist.TablistText;
import com.fadelands.array.punishments.PunishmentManager;
import com.fadelands.array.punishments.commands.*;
import com.fadelands.array.punishments.PunishmentMenu;
import com.fadelands.array.settings.Settings;
import com.fadelands.array.settings.SettingsCommandExecutor;
import com.fadelands.array.staff.StaffMode;
import com.fadelands.array.staff.StaffSettings;
import com.fadelands.array.staff.command.StaffCommand;
import com.fadelands.array.staff.command.StaffSettingsCommand;
import com.fadelands.array.staff.command.VanishCommand;
import com.fadelands.array.staff.inventory.SettingsInventory;
import com.fadelands.array.staff.inventory.StaffInventory;
import com.fadelands.array.utils.PluginMessage;
import com.fadelands.array.utils.ServerStatistics;
import com.fadelands.array.utils.Utils;
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
public class Array extends JavaPlugin {

    private long serverStartTime = System.currentTimeMillis();

    private OkHttpClient okHttpClient;
    private PluginMessage pluginMessage;
    private PunishmentMenu punishmentMenu;
    private PunishmentManager punishmentManager;
    private GeoManager geoManager;
    private ServerManager serverManager;
    private PlayerManager playerManager;
    private DatabaseManager databaseManager;
    private NPCManager npcManager;
    private ServerStatistics serverStats;
    private StaffSettings staffSettings;
    private LuckPermsApi luckPerms;
    private Settings settings;
    private ChatProvider chatProvider;
    private com.fadelands.array.provider.chat.Chat serverChat;
    private SimpleboardManager simpleboardManager;
    private Announcements announcements;

    private Economy econ = null;
    private Permission perms = null;
    private Chat chat = null;

    public static Array plugin;

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

        Bukkit.getLogger().info("[Array] Plugin has been enabled.");
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
        this.serverStats = new ServerStatistics();
        this.staffSettings = new StaffSettings();
        this.settings = new Settings();
        this.npcManager = new NPCManager();
        this.serverChat = new com.fadelands.array.provider.chat.Chat(this);
        this.announcements = new Announcements(this, getSettings(), Array.plugin.getPluginMessage());

        PluginManager pm = Bukkit.getPluginManager(); //can not be placed above

        pm.registerEvents(new PunishmentManager(this), this);
        pm.registerEvents(new ServerManager(this), this);
        pm.registerEvents(new PlayerManager(this), this);
        pm.registerEvents(new PunishmentMenu(this), this);
        pm.registerEvents(new SettingsInventory(this), this);
        pm.registerEvents(new StaffInventory(this), this);
        pm.registerEvents(new StaffMode(getStaffSettings()), this);
        pm.registerEvents(new ProfileInventory(this), this);
        pm.registerEvents(new com.fadelands.array.settings.inventory.SettingsInventory(this), this);
        pm.registerEvents(new Events(this), this);
        pm.registerEvents(new HelpInventory(this), this);
        pm.registerEvents(new ApplyGui(this), this);
        pm.registerEvents(new ServerStatsInventory(this, Array.plugin.getServerStats()), this);
        pm.registerEvents(new GuideMenu(this), this);
        pm.registerEvents(new DiscordLinkGuide(this), this);
        pm.registerEvents(new TablistText(this), this);
        /*
        pm.registerEvents(new CountMessages(this), this);
        pm.registerEvents(new CountLogins(this), this);
        pm.registerEvents(new CountBlocksPlaced(this), this);
        pm.registerEvents(new CountBlocksRemoved(this), this);
        pm.registerEvents(new CountKills(this), this);
        pm.registerEvents(new SaveOnQuit(this), this);
        pm.registerEvents(new LoadPlayerData(), this);
        pm.registerEvents(new CountDeaths(this), this);
        new AutoSaveDb().runTaskTimer(this, 2 * 60 * 20, 2 * 60 * 20);
        */
        //todo: ^^ above is temporarily disabled

        pm.registerEvents(new com.fadelands.array.provider.chat.Chat(this), this);
        pm.registerEvents(getSimpleboardManager(), this);
        pm.registerEvents(new AnnouncementListener(this), this);
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
            Bukkit.getLogger().warning("[Array] Something went wrong when attempting to load LuckPerms API.");
        } else {
            luckPerms = provider.getProvider();
            Bukkit.getLogger().warning("[Array] Loaded LuckPerms API.");
        }
    }

    public void onDisable() {
        getDatabaseManager().shutdown();
        Bukkit.getLogger().info("[Array] Plugin has been disabled.");
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

    public com.fadelands.array.provider.chat.Chat getServerChat() {
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
