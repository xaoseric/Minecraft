package com.fadelands.lobby.commands;

import com.fadelands.lobby.Main;
import com.fadelands.lobby.guis.LobbySettingsGui;
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
        LobbySettingsGui lobbySettingsGui = new LobbySettingsGui(main);
        lobbySettingsGui.openLobbySettings(player);
        return false;
    }
}
