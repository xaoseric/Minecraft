package com.fadelands.essentials;

import com.fadelands.array.Array;
import com.fadelands.essentials.commands.admin.*;
import com.fadelands.essentials.commands.admin.inventory.WhoisInventory;
import com.fadelands.essentials.commands.mod.FlyCommand;
import com.fadelands.essentials.commands.mod.TeleportCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private Array instance;

    public void onEnable() {
        Bukkit.getLogger().info("[Essentials] Starting up plugin.");
        instance = Array.plugin;

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

    }

    public void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new WhoisInventory(this), this);


    }

    public void onDisable() {
        Bukkit.getLogger().info("[Essentials] Plugin has been disabled.");

    }

    public Array getInstance() {
        return instance;
    }
}
