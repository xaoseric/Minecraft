package com.fadelands.lobby;

import com.fadelands.array.Array;
import com.fadelands.array.utils.Utils;
import com.fadelands.core.CorePlugin;
import com.fadelands.core.scoreboard.SimpleboardManager;
import com.fadelands.lobby.commands.BuildModeCommandExecutor;
import com.fadelands.lobby.commands.LobbySettingsCommandExecutor;
import com.fadelands.lobby.events.*;
import com.fadelands.lobby.guis.ServerSelectorGui;
import com.fadelands.lobby.guis.SkyblockGui;
import com.fadelands.lobby.scoreboard.LobbyBoardProvider;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Main extends JavaPlugin implements Listener {

    public static final Logger log = Logger.getLogger("Minecraft");

    private static Main instance;

    public List<Player> buildMode = new ArrayList<Player>();
    private static Economy econ = null;
    private static Permission perms = null;
    private static Chat chat = null;

    public void onEnable() {
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        setInstance(this);

        Plugin array = Bukkit.getPluginManager().getPlugin("Array");
        if(array == null){
            getLogger().warning("Couldn't find Array. Stopping server.");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "stop");
        }

        getLogger().info("Plugin has successfully been enabled.");

        /*
        Register Events
        */

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(this, this);
        pm.registerEvents(new JoinEvent(this), this);
        pm.registerEvents(new QuitEvent(this), this);
        pm.registerEvents(new JoinItems(this), this);
        pm.registerEvents(new ServerSelectorGui(this), this);
        pm.registerEvents(new SkyblockGui(this), this);
        pm.registerEvents(new NoSaturation(this), this);
        pm.registerEvents(new NoRain(this), this);
        pm.registerEvents(new NoDamage(this), this);
        pm.registerEvents(new NoItemDrops(this), this);
        pm.registerEvents(new DisableVoid(this), this);
        pm.registerEvents(new NoBlockBreak(this), this);
        pm.registerEvents(new NoBlockPlace(this), this);
        pm.registerEvents(new NoInvMove(this), this);
        pm.registerEvents(new NoPickup(this), this);

        SimpleboardManager simpleboardManager = CorePlugin.getInstance().getSimpleboardManager();
        simpleboardManager.setBoardProvider(new LobbyBoardProvider(Array.plugin.getPluginMessage()));

        /*
        Register Commands
         */
        getCommand("buildmode").setExecutor(new BuildModeCommandExecutor(this));
        getCommand("lobbysettings").setExecutor(new LobbySettingsCommandExecutor(this));

        if (!setupEconomy() ) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + Utils.Core + "Disabled due to no Vault dependency found! - " + getDescription().getName());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        setupChat();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + Utils.Core + "Vault API hooked into the plugin.");

    }
    @SuppressWarnings("Duplicates")
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

    public static Main getInstance() {
        return instance;
    }

    private static void setInstance(Main instance) {
        Main.instance = instance;
    }

    public void onDisable() {
        getLogger().info("Plugin has been disabled.");
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
}
