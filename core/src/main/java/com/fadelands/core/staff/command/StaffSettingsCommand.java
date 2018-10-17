package com.fadelands.core.staff.command;

import com.fadelands.core.Core;
import com.fadelands.core.player.UserUtil;
import com.fadelands.core.staff.inventory.SettingsInventory;
import com.fadelands.core.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffSettingsCommand implements CommandExecutor {

    private Core core;

    public StaffSettingsCommand(Core core) {
        this.core = core;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cThis can only be used ingame");
            return true;
        }

        Player player = (Player) sender;
        if(!UserUtil.isStaff(player.getName())) {
            sender.sendMessage(Utils.No_Perm);
            return true;
        }

        SettingsInventory inv = new SettingsInventory(core);
        inv.openInventory(player);

        return false;
    }
}
