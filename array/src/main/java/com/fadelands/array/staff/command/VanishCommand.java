package com.fadelands.array.staff.command;

import com.fadelands.array.Array;
import com.fadelands.array.database.SQLUtils;
import com.fadelands.array.player.User;
import com.fadelands.array.staff.StaffMode;
import com.fadelands.array.staff.StaffSettings;
import com.fadelands.array.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor {

    private Array array;
    private StaffMode staffMode;
    private StaffSettings settings;

    public VanishCommand(Array array, StaffMode staffMode, StaffSettings settings) {
        this.array = array;
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
        if(!(new User().isStaff(player.getName()))) {
            player.sendMessage(Utils.No_Perm);
            return true;
        }

        if(settings.vanishOn(player)) {
            staffMode.toggleOff(player);
            System.out.println("toggled off for " + player.getName());
            SQLUtils.updateTable(player, "staff_settings", "vanish_toggled", false);
        } else {
            staffMode.toggleOn(player);
            System.out.println("toggled on for " + player.getName());
            SQLUtils.updateTable(player, "staff_settings", "vanish_toggled", true);
        }
        return false;
    }
}
