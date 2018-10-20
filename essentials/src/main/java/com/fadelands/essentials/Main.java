package com.fadelands.essentials;

import com.fadelands.core.Core;
import com.fadelands.essentials.commands.admin.*;
import com.fadelands.essentials.commands.admin.inventory.WhoisInventory;
import com.fadelands.essentials.commands.mod.FlyCommand;
import com.fadelands.essentials.commands.mod.TeleportCommand;
import com.fadelands.essentials.warps.Warps;
import com.fadelands.essentials.warps.command.AddWarpCommand;
import com.fadelands.essentials.warps.command.RemoveWarpCommand;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {

    private File warpsFile;
    private FileConfiguration warpsConfig;

    private Warps warps;

    public static Main instance;

    public void onEnable() {
        instance = this;
        Bukkit.getLogger().info("[Essentials] Starting up plugin.");

        this.saveDefaultConfig();
        this.createFiles();
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
        getCommand("addwarp").setExecutor(new AddWarpCommand(this));
        getCommand("removewarp").setExecutor(new RemoveWarpCommand(this));

    }

    public void registerListeners() {

        this.warps = new Warps(this);

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new WhoisInventory(this), this);
    }

    public void createFiles() {
        warpsFile = new File(getDataFolder(), "warps.yml");
        if (!(warpsFile.exists())) {
            //noinspection ResultOfMethodCallIgnored
            warpsFile.getParentFile().mkdirs();
            saveResource("warps.yml", false);
        }
        warpsConfig = new YamlConfiguration();
        try {
            warpsConfig.load(warpsFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void onDisable() {
        Bukkit.getLogger().info("[Essentials] Plugin has been disabled.");
    }

    public File getWarpsFile() {
        return warpsFile;
    }

    public FileConfiguration getWarpsConfig() {
        return warpsConfig;
    }

    public Warps getWarps() {
        return warps;
    }

    public Main getInstance() {
        return instance;
    }
}
