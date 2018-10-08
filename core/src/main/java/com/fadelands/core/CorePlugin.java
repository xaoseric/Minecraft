package com.fadelands.core;

import com.fadelands.array.Array;
import com.fadelands.array.utils.Utils;
import com.fadelands.core.achievement.AchievementCommand;
import com.fadelands.core.commands.CommandProcess;
import com.fadelands.core.commands.ListCommand;
import com.fadelands.core.commands.help.command.GuidesCommandExecutor;
import com.fadelands.core.commands.help.guides.DiscordLinkGuide;
import com.fadelands.core.commands.help.guides.GuideMenu;
import com.fadelands.core.commands.help.inventory.ServerStatsInventory;
import com.fadelands.core.commands.staff.admincmds.GameModeCommand;
import com.fadelands.core.commands.staff.admincmds.SudoCommand;
import com.fadelands.core.commands.staff.modcmds.FlyCommand;
import com.fadelands.core.commands.staff.modcmds.TeleportCommand;
import com.fadelands.core.commands.help.inventory.ApplyGui;
import com.fadelands.core.commands.help.command.HelpCommandExecutor;
import com.fadelands.core.commands.help.inventory.HelpInventory;
import com.fadelands.core.events.Events;
import com.fadelands.core.profile.command.ProfileCommand;
import com.fadelands.core.profile.inventory.ProfileInventory;
import com.fadelands.core.provider.chat.announcements.AnnouncementListener;
import com.fadelands.core.provider.chat.announcements.Announcements;
import com.fadelands.core.provider.chat.command.ChatSlowCommandExecutor;
import com.fadelands.core.provider.chat.command.SilenceChatCommandExecutor;
import com.fadelands.core.provider.scoreboard.SimpleBoardProvider;
import com.fadelands.core.provider.scoreboard.SimpleboardManager;
import com.fadelands.core.provider.chat.SimpleChatProvider;
import com.fadelands.core.provider.chat.provider.ChatProvider;
import com.fadelands.core.provider.tablist.TablistText;
import com.fadelands.core.settings.Settings;
import com.fadelands.core.settings.SettingsCommandExecutor;
import com.fadelands.core.settings.inventory.SettingsInventory;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

@SuppressWarnings("ALL")
public class CorePlugin extends JavaPlugin {

    private static CorePlugin instance;

    public static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ = null;
    private static Permission perms = null;
    private static Chat chat = null;
    private com.fadelands.core.provider.chat.Chat serverChat;
    private SimpleboardManager simpleboardManager;
    private ChatProvider chatProvider;
    private Settings settings;
    private Announcements announcements;

    public void onEnable() {
        instance = this;
        this.chatProvider = new SimpleChatProvider(this);

        Bukkit.getConsoleSender().sendMessage("[Core] Make sure this server is running core plugin Array. This server does not work without it.");

        //checking if the server has the plugin
        Bukkit.getConsoleSender().sendMessage("[Core] Attemping to find core plugin Array.");
        Plugin array = Bukkit.getServer().getPluginManager().getPlugin("Array");
        if (array == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[Core] Couldn't find plugin Array. This server can't run without it. Stopping the server.");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "stop");
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + Utils.Core + "Array found and loaded, yay!");

        registerCommands();
        registerEvents();
        saveDefaultConfig();

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + Utils.Core + "Plugin has been enabled.");

        if (!setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + Utils.Core + "Disabled due to no Vault dependency found! - " + getDescription().getName());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        setupChat();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + Utils.Core + "Vault API hooked into the plugin.");
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();

        // Profile & Players
        pm.registerEvents(new ProfileInventory(this), this);
        pm.registerEvents(new SettingsInventory(this), this);
        pm.registerEvents(new Events(this), this);

        //GUIDES & HELP

        pm.registerEvents(new HelpInventory(this), this);
        pm.registerEvents(new ApplyGui(this), this);
        pm.registerEvents(new ServerStatsInventory(this, Array.plugin.getServerStats()), this);
        pm.registerEvents(new GuideMenu(this), this);
        pm.registerEvents(new DiscordLinkGuide(this), this);

        // Tablist
        pm.registerEvents(new TablistText(this), this);

        // PlayerData Classes

        /* pm.registerEvents(new CountMessages(this), this);
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


        pm.registerEvents(new com.fadelands.core.provider.chat.Chat(this), this);

        this.serverChat = new com.fadelands.core.provider.chat.Chat(this);
        this.settings = new Settings();

        simpleboardManager = new SimpleboardManager(this, new SimpleBoardProvider());
        simpleboardManager.runTaskTimerAsynchronously(this, 2L, 2L);
        pm.registerEvents(simpleboardManager, this);

        announcements = new Announcements(this, getSettings(), Array.plugin.getPluginMessage());
        pm.registerEvents(new AnnouncementListener(this), this);

        pm.registerEvents(new CommandProcess(this), this);

    }

    private void registerCommands() {
        getCommand("profile").setExecutor(new ProfileCommand(this));
        getCommand("achievement").setExecutor(new AchievementCommand(this));

        // Mod cmds
        getCommand("teleport").setExecutor(new TeleportCommand(this));
        getCommand("fly").setExecutor(new FlyCommand(this));
        getCommand("chatslow").setExecutor(new ChatSlowCommandExecutor(this));
        getCommand("silencechat").setExecutor(new SilenceChatCommandExecutor(this));

        // Admin cmds
        getCommand("gamemode").setExecutor(new GameModeCommand(this));
        getCommand("sudo").setExecutor(new SudoCommand(this));

        //Help
        getCommand("help").setExecutor(new HelpCommandExecutor(this));
        getCommand("guides").setExecutor(new GuidesCommandExecutor(this));
        getCommand("settings").setExecutor(new SettingsCommandExecutor(this));
        getCommand("list").setExecutor(new ListCommand(this));

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

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + Utils.Core + "Plugin has been disabled.");

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

    public SimpleboardManager getSimpleboardManager() {
        return simpleboardManager;
    }

    public static CorePlugin getInstance() {
        return instance;
    }

    public ChatProvider getChatProvider() {
        return this.chatProvider;
    }

    public void setChatProvider(ChatProvider chatProvider) {
        this.chatProvider = chatProvider;
    }

    public Settings getSettings() {
        return settings;
    }

    public Announcements getAnnouncements() {
        return announcements;
    }

    public com.fadelands.core.provider.chat.Chat getServerChat() {
        return this.serverChat;
    }
}
