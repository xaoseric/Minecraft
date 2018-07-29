package com.fadelands.bungeecore.commands.servers;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BuildCommand extends Command {

    public BuildCommand(){
        super("build", null);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        ProxiedPlayer player = (ProxiedPlayer) commandSender;

            ServerInfo targetServer = ProxyServer.getInstance().getServerInfo("BUILD");
            player.connect(targetServer);
        }
    }

