package com.fadelands.lobby.commands;

import com.fadelands.array.Array;
import com.fadelands.core.CorePlugin;
import com.fadelands.core.settings.inventory.SettingsInventory;
import com.fadelands.lobby.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbySettingsCommandExecutor implements CommandExecutor {

    private Main main;

    public LobbySettingsCommandExecutor(Main main) {
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return true;
        Player player = (Player) commandSender;
        SettingsInventory settingsInventory = new SettingsInventory(CorePlugin.getInstance());
        settingsInventory.openLobbySettings(player);
        return false;
    }
}
