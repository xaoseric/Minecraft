package com.fadelands.array.staff.command;

import com.fadelands.array.Array;
import com.fadelands.array.player.User;
import com.fadelands.array.staff.inventory.SettingsInventory;
import com.fadelands.array.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffSettingsCommand implements CommandExecutor {

    private Array array;

    public StaffSettingsCommand(Array array) {
        this.array = array;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cThis can only be used ingame");
            return true;
        }
        User user = new User();
        Player player = (Player) sender;
        if(!user.isStaff(player.getName())) {
            sender.sendMessage(Utils.No_Perm);
            return true;
        }

        SettingsInventory inv = new SettingsInventory(array);
        inv.openInventory(player);

        return false;
    }
}
