package com.fadelands.sbair;

import com.fadelands.array.Array;
import com.fadelands.core.CorePlugin;
import com.fadelands.core.provider.scoreboard.SimpleboardManager;
import com.fadelands.sbair.scoreboard.SBAirBoardProvider;
import com.fadelands.sbair.serverchat.SBAirChatProvider;
import com.fadelands.sbair.skyblockmanager.IslandManagerCommand;
import com.fadelands.sbair.skyblockmanager.IslandMenu;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Main extends JavaPlugin {

    public static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ = null;
    private static Permission perms = null;
    private static Chat chat = null;
    public static ASkyBlockAPI asbAPI;

    public void onEnable() {
        getInstance();
        getLogger().info("Plugin has successfully been enabled.");

        /*
        Register Events
         */

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new IslandMenu(this), this);

        SimpleboardManager simpleboardManager = CorePlugin.getInstance().getSimpleboardManager();
        simpleboardManager.setBoardProvider(new SBAirBoardProvider(Array.plugin.getPluginMessage()));

        CorePlugin.getInstance().setChatProvider(new SBAirChatProvider());

        // Regc ommands
        getCommand("islandmenu").setExecutor(new IslandManagerCommand(this));

        // Vault
        if (!setupEconomy()) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        setupChat();
        Bukkit.getLogger().info("Vault API loaded successfully.");
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

    public static ASkyBlockAPI getInstance() {
        asbAPI = ASkyBlockAPI.getInstance();
        Plugin asykblock = Bukkit.getPluginManager().getPlugin("askyblock");
        if (asykblock != null) {
        }
        Bukkit.getLogger().info("Hooked into aSkyBlock!");
        return asbAPI;

    }
}



