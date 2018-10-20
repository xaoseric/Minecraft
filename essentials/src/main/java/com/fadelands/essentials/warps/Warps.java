package com.fadelands.essentials.warps;

import com.fadelands.core.utils.Utils;
import com.fadelands.essentials.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import sun.security.krb5.Config;

import java.io.File;
import java.io.IOException;

public class Warps {

    private Main plugin;

    public Warps(Main plugin) {
        this.plugin = plugin;
    }

    public void addWarp(Location location, String name) {

        FileConfiguration config = plugin.getWarpsConfig();
        config.set("warps." + name + ".world", location.getWorld().getName());
        config.set("warps." + name + ".x", location.getX());
        config.set("warps." + name + ".y", location.getY());
        config.set("warps." + name + ".z", location.getZ());
        config.set("warps." + name + ".yaw", location.getYaw());
        config.set("warps." + name + ".pitch", location.getPitch());
        config.set("warps." + name, null);

        try {
            config.save(plugin.getDataFolder() + File.separator + "warps.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeWarp(String name) {

        FileConfiguration config = plugin.getWarpsConfig();
        config.set("warps." + name + ".world", null);
        config.set("warps." + name + ".x", null);
        config.set("warps." + name + ".y", null);
        config.set("warps." + name + ".z", null);
        config.set("warps." + name + ".yaw", null);
        config.set("warps." + name + ".pitch", null);

        try {
            config.save(plugin.getDataFolder() + File.separator + "warps.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void warp(Player player, String warp) {

        if (!warpExists(warp)) {
            player.sendMessage(Utils.Prefix + "§cThat warp doesn't exist.");
            return;
        }

        ConfigurationSection configSection = plugin.getWarpsConfig().getConfigurationSection("warps").getConfigurationSection(warp);

        float yaw = (float) configSection.getDouble("yaw");
        float pitch = (float) configSection.getDouble("pitch");

        Location location = new Location(Bukkit.getWorld(configSection.getString("world")),
                configSection.getDouble("x"), configSection.getDouble("y"),
                configSection.getDouble("z"), yaw, pitch);

        player.teleport(location);
    }

    public boolean warpExists(String warp) {
        for (String items : plugin.getWarpsConfig().getConfigurationSection("warps").getKeys(false)) {
            if (items.equalsIgnoreCase(warp)) return true;
        }
        return false;
    }
}
