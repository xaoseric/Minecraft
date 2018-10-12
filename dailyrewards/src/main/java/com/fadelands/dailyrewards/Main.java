package com.fadelands.dailyrewards;

import com.fadelands.core.utils.Utils;
import com.fadelands.dailyrewards.rewardman.RewardManListener;
import com.fadelands.dailyrewards.rewardman.command.RewardManCommand;
import com.fadelands.dailyrewards.rewardman.command.RewardsCommand;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    private static Main instance;

    public static final Logger log = Logger.getLogger("Minecraft");
    private static Economy econ = null;
    private static Permission perms = null;
    private static Chat chat = null;

    private File rewardManFile;
    private FileConfiguration rewardManConfig;

    public void onEnable() {
        instance = this;

        Bukkit.getConsoleSender().sendMessage("[DailyRewards] Make sure this server is running core plugin Core. This server does not work without it.");

        //checking if the server has the plugin
        Bukkit.getConsoleSender().sendMessage("[DailyRewards] Attempting to find core plugin Core.");
        Plugin array = Bukkit.getServer().getPluginManager().getPlugin("Core");
        if (array == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[DailyRewards] Couldn't find plugin Core. This server can't run without it. Stopping the server.");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "stop");
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + Utils.DailyRewards + "Core found and loaded, yay!");

        registerCommands();
        registerEvents();
        createRewardManFile();

        if (!setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + Utils.DailyRewards + "Disabled due to no Vault dependency found! - " + getDescription().getName());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        setupPermissions();
        setupChat();
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + Utils.DailyRewards + "Vault API hooked into the plugin.");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + Utils.DailyRewards + "Plugin has been enabled.");
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new RewardManListener(this), this);

    }

    private void registerCommands() {
        getCommand("rewardman").setExecutor(new RewardManCommand(this));
        getCommand("rewards").setExecutor(new RewardsCommand(this));

    }

    private void createRewardManFile() {
        rewardManFile = new File(getDataFolder(), "rewardman.yml");
        if (!(rewardManFile.exists())) {
            rewardManFile.getParentFile().mkdirs();
            saveResource("rewardman.yml", false);
        }
        rewardManConfig = new YamlConfiguration();
        try {
            rewardManConfig.load(rewardManFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
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
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + Utils.DailyRewards + "Plugin has been disabled.");

    }

    public Main getInstance() {
        return instance;
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

    public FileConfiguration getRewardManFile() {
        return rewardManConfig;
    }

}
