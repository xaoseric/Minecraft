package com.fadelands.core.staff.command;

import com.fadelands.core.Core;
import com.fadelands.core.player.User;
import com.fadelands.core.staff.StaffMode;
import com.fadelands.core.staff.StaffSettings;
import com.fadelands.core.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor {

    private Core core;
    private StaffMode staffMode;
    private StaffSettings settings;

    public VanishCommand(Core core, StaffMode staffMode, StaffSettings settings) {
        this.core = core;
        this.staffMode = staffMode;
        this.settings = settings;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cThis can only be used ingame.");
            return true;
        }

        Player player = (Player) sender;
        if(!(User.isStaff(player.getName()))) {
            player.sendMessage(Utils.No_Perm);
            return true;
        }

        if(settings.vanishOn(player)) {
            staffMode.toggleOff(player);
            System.out.println("toggled off for " + player.getName());
            core.getDatabaseManager().updateTable(player, "staff_settings", "vanish_toggled", false);
        } else {
            staffMode.toggleOn(player);
            System.out.println("toggled on for " + player.getName());
            core.getDatabaseManager().updateTable(player, "staff_settings", "vanish_toggled", true);
        }
        return false;
    }
}
