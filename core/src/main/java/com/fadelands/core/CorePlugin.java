package com.fadelands.core;

import com.fadelands.array.utils.Utils;
import com.fadelands.core.achievement.AchievementCommand;
import com.fadelands.core.commands.CommandProcess;
import com.fadelands.core.commands.CountryCommandExecutor;
import com.fadelands.core.commands.admincmds.GameModeCommandExecutor;
import com.fadelands.core.commands.admincmds.SudoCommandExecutor;
import com.fadelands.core.commands.amodcmds.FlyCommandExecutor;
import com.fadelands.core.commands.amodcmds.TeleportCommandExecutor;
import com.fadelands.core.commands.help.ApplyGui;
import com.fadelands.core.commands.help.HelpCommandExecutor;
import com.fadelands.core.commands.help.HelpInventory;
import com.fadelands.core.commands.playerlist.ListCommandExecutor;
import com.fadelands.core.commands.playerlist.ListCommandHandler;
import com.fadelands.core.npcs.CreateNPC;
import com.fadelands.core.playerdata.*;
import com.fadelands.core.profile.ProfileCommandExecutor;
import com.fadelands.core.profile.ProfileListener;
import com.fadelands.core.profile.statistics.StatsListener;
import com.fadelands.core.scoreboard.SimpleBoardProvider;
import com.fadelands.core.scoreboard.SimpleboardManager;
import com.fadelands.core.serverchat.ChatListener;
import com.fadelands.core.serverchat.SimpleChatProvider;
import com.fadelands.core.serverchat.provider.ChatProvider;
import com.fadelands.core.tablist.TablistText;
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
    private SimpleboardManager simpleboardManager;
    private ChatProvider chatProvider;


    public void onEnable() {
        instance = this;
        this.chatProvider = new SimpleChatProvider();

        Bukkit.getConsoleSender().sendMessage("[FadeLandsCore] Make sure this server is running core plugin Array. This server does not work without it.");

        //checking if the server has the plugin
        Bukkit.getConsoleSender().sendMessage("[FadeLandsCore] Attemping to find core plugin Array.");
        Plugin array = Bukkit.getServer().getPluginManager().getPlugin("Array");
        if (array == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[FadeLandsCore] Couldn't find plugin Array. This server can't run without it. Stopping the server.");
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
        pm.registerEvents(new ProfileListener(), this);
        pm.registerEvents(new StatsListener(), this);

        pm.registerEvents(new HelpInventory(), this);
        pm.registerEvents(new ApplyGui(this), this);

        //   pm.registerEvents(new NPCs(), this);

        // Tablist
        pm.registerEvents(new TablistText(this), this);

        // PlayerData Classes
        pm.registerEvents(new CountMessages(this), this);
        pm.registerEvents(new CountCommands(this), this);
        pm.registerEvents(new CountLogins(this), this);
        pm.registerEvents(new CountBlocksPlaced(this), this);
        pm.registerEvents(new CountBlocksRemoved(this), this);
        pm.registerEvents(new CountKills(this), this);
        pm.registerEvents(new SaveOnQuit(this), this);
        pm.registerEvents(new LoadPlayerData(), this);
        //pm.registerEvents(new CountDeaths(this), this);

        pm.registerEvents(new ChatListener(), this);

        new AutoSaveDb().runTaskTimer(this, 2 * 60 * 20, 2 * 60 * 20);

        simpleboardManager = new SimpleboardManager(this, new SimpleBoardProvider());
        pm.registerEvents(simpleboardManager, this);
        simpleboardManager.runTaskTimerAsynchronously(this, 2L, 2L);

        pm.registerEvents(new CommandProcess(this), this);

    }

    private void registerCommands() {
        getCommand("profile").setExecutor(new ProfileCommandExecutor(this));
        getCommand("saveplayerdata").setExecutor(new SavePlayerData(this));
        getCommand("achievement").setExecutor(new AchievementCommand(this));

        // Mod cmds:
        getCommand("teleport").setExecutor(new TeleportCommandExecutor(this));
        getCommand("fly").setExecutor(new FlyCommandExecutor(this));

        // Admin cmds:
        getCommand("gamemode").setExecutor(new GameModeCommandExecutor(this));
        getCommand("sudo").setExecutor(new SudoCommandExecutor(this));

        //Other

        getCommand("createnpc").setExecutor(new CreateNPC(this));

        getCommand("country").setExecutor(new CountryCommandExecutor(this));

        getCommand("help").setExecutor(new HelpCommandExecutor(this));

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

    public static Economy getEconomy() {
        return econ;
    }

    public static Permission getPermissions() {
        return perms;
    }

    public static Chat getChat() {
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
}
