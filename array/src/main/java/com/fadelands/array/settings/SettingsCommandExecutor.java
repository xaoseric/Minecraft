package com.fadelands.array.settings;

import com.fadelands.array.Array;
import com.fadelands.array.settings.inventory.SettingsInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettingsCommandExecutor implements CommandExecutor {

    private Array core;

    public SettingsCommandExecutor(Array core){
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
