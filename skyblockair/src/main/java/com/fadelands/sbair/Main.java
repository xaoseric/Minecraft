package com.fadelands.sbair;

import com.fadelands.core.Core;
import com.fadelands.core.provider.scoreboard.SimpleboardManager;
import com.fadelands.sbair.provider.scoreboard.SBBoardProvider;
import com.fadelands.sbair.provider.chat.SBChatProvider;
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
    private Economy econ = null;
    private Permission perms = null;
    private Chat chat = null;
    private ASkyBlockAPI skyBlockApi;

    public void onEnable() {
        this.skyBlockApi = ASkyBlockAPI.getInstance();
        Plugin sb = Bukkit.getPluginManager().getPlugin("askyblock");
        if (sb != null) {
            Bukkit.getLogger().info("[SkyBlockAir] Hooked into aSkyBlock!");
        }

        /*
        Register Events
         */

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new IslandMenu(this), this);

        SimpleboardManager simpleboardManager = Core.plugin.getSimpleboardManager();
        simpleboardManager.setBoardProvider(new SBBoardProvider(Core.plugin.getPluginMessage(), this));

        Core.plugin.setChatProvider(new SBChatProvider(this));

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
        Bukkit.getLogger().info("[SkyBlockAir] Vault API loaded successfully.");
        getLogger().info("[SkyBlockAir] Plugin has successfully been enabled.");
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
        getLogger().info("[SkyBlockAir] Plugin has been disabled.");
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

    public ASkyBlockAPI getSkyBlockApi() {
        return skyBlockApi;

    }
}



