package com.fadelands.core.settings;

import com.fadelands.core.Core;
import com.fadelands.core.settings.inventory.SettingsInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettingsCommandExecutor implements CommandExecutor {

    private Core core;

    public SettingsCommandExecutor(Core core){
        this.core = core;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("no console");
            return true;
        }
        Player player = (Player) sender;
        SettingsInventory inv = new SettingsInventory(core);
        inv.openInventory(player);

        return false;
    }
}
