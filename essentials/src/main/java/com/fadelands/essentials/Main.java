package com.fadelands.essentials;

import com.fadelands.core.Core;
import com.fadelands.essentials.commands.admin.*;
import com.fadelands.essentials.commands.admin.inventory.WhoisInventory;
import com.fadelands.essentials.commands.mod.FlyCommand;
import com.fadelands.essentials.commands.mod.TeleportCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public void onEnable() {
        Bukkit.getLogger().info("[Essentials] Starting up plugin.");

        this.saveDefaultConfig();
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        registerListeners();
        registerCommands();

        Bukkit.getLogger().info("[Essentials] Plugin has been enabled.");
    }

    public void registerCommands() {
        getCommand("whois").setExecutor(new WhoIsCommand(this));
        getCommand("country").setExecutor(new CountryCommand(this));
        getCommand("teleport").setExecutor(new TeleportCommand(this));
        getCommand("fly").setExecutor(new FlyCommand(this));
        getCommand("gamemode").setExecutor(new GameModeCommand(this));
        getCommand("sudo").setExecutor(new SudoCommand(this));
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("feed").setExecutor(new FeedCommand());
        getCommand("toggledownfall").setExecutor(new ToggledownfallCommand());

    }

    public void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new WhoisInventory(this), this);


    }

    public void onDisable() {
        Bukkit.getLogger().info("[Essentials] Plugin has been disabled.");

    }

    public Core getInstance() {
        return Core.plugin;
    }
}
