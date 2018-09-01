package com.fadelands.core.settings;

import com.fadelands.core.CorePlugin;
import com.fadelands.core.settings.inventory.SettingsInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettingsCommandExecutor implements CommandExecutor {

    private CorePlugin core;

    public SettingsCommandExecutor(CorePlugin core){
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
