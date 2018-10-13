package com.fadelands.core.commands;

import com.fadelands.core.Core;
import com.fadelands.core.player.User;
import com.fadelands.core.staff.StaffSettings;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ListCommand implements CommandExecutor {

    private Core plugin;

    public ListCommand(Core plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        sender.sendMessage("§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8= \n" +
                "§6Players Online: §f" + getGlobalPlayers() + " §7(" + Bukkit.getOnlinePlayers().size() + " here)\n" +
                "§r \n" +
                "§6Staff Online: §7" + Arrays.toString(getStaffOnline()).replace("[", "").replace("]", "") + "\n" +
                "§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=");
        return false;
    }

    private String[] getStaffOnline() {
        for (Player staff : Bukkit.getOnlinePlayers()) {
            if (User.isStaff(staff.getName())) {
                if (!(new StaffSettings().vanishOn(staff))) {
                    return staff.getName().split(", ");
                }
            }
        }
        return new String[]{"None."};
    }

    private int getGlobalPlayers() {
        return plugin.getPluginMessage().getPlayers("ALL");
    }
}
