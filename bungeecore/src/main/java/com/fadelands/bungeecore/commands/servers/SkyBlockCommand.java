package com.fadelands.bungeecore.commands.servers;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SkyBlockCommand extends Command {

    public SkyBlockCommand(){
        super("skyblock", null, "sb");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        ProxiedPlayer player = (ProxiedPlayer) commandSender;

        if (strings.length == 0) {
            player.sendMessage(new ComponentBuilder("§cYou have to specify a Skyblock Realm! \n  §6Available Realms: AIR & WATER").color(ChatColor.GOLD).create());
            return;
        }
        if (strings[0].equalsIgnoreCase("air")) {
            ServerInfo targetServer = ProxyServer.getInstance().getServerInfo("SB-AIR");
            player.connect(targetServer);
            return;
        }
        if (strings[0].equalsIgnoreCase("water")) {
            ServerInfo targetServer = ProxyServer.getInstance().getServerInfo("SB-WATER");
            player.connect(targetServer);
            return;
        }
    }
}
