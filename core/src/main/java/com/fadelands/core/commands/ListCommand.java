package com.fadelands.core.commands;

import com.fadelands.array.Array;
import com.fadelands.array.player.User;
import com.fadelands.array.staff.StaffSettings;
import com.fadelands.core.CorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ListCommand implements CommandExecutor {

    private CorePlugin plugin;

    public ListCommand(CorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        sender.sendMessage("§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8= \n" +
                "§6Players Online: §f" + Array.plugin.getPluginMessage().getPlayers("ALL") + " §7(" + Bukkit.getOnlinePlayers().size() + " here)\n" +
                "§r \n" +
                "§6Staff Online: §7" + Arrays.toString(getStaffOnline()).replace("[", "").replace("]", "") + "\n" +
                "§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=§6=§8=");
        return false;
    }

    public String[] getStaffOnline() {
        for (Player staff : Bukkit.getOnlinePlayers()) {
            if (new User().isStaff(staff.getName())) {
                if (!(new StaffSettings().vanishOn(staff))) {
                    return staff.getName().split(", ");
                }
            }
        }
        return new String[]{"None"};
    }
}
