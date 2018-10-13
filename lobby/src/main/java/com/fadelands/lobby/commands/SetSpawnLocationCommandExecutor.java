package com.fadelands.lobby.commands;

import com.fadelands.core.player.User;
import com.fadelands.core.utils.Utils;
import com.fadelands.lobby.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class SetSpawnLocationCommandExecutor implements CommandExecutor {

    private Main plugin;

    public SetSpawnLocationCommandExecutor(Main main){
        this.plugin = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("u rly think this would work in the console?");
            return true;
        }

        if(!(User.isAdmin(sender.getName()))){
            sender.sendMessage(Utils.No_Perm);
            return true;
        }

        Player player = (Player) sender;

        Location location = player.getLocation();
        plugin.getConfig().set("x", location.getX());
        plugin.getConfig().set("y", location.getY());
        plugin.getConfig().set("z", location.getZ());
        plugin.getConfig().set("yaw", location.getYaw());
        plugin.getConfig().set("pitch", location.getPitch());
        plugin.getConfig().set("world", location.getWorld().getName());
        File file = new File(plugin.getDataFolder() + File.separator + "config.yml");
        try {
            plugin.getConfig().save(file);
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage("§cCouldn't save spawn location to configuration file.");
        }

        player.sendMessage(ChatColor.AQUA + "Lobby spawn location set.");

        return false;
    }
}
