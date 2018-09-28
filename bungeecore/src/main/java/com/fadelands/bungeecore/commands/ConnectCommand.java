package com.fadelands.bungeecore.commands;

import com.fadelands.bungeecore.Main;
import com.fadelands.bungeecore.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ConnectCommand extends Command {

    private Main main;

    public ConnectCommand(Main main) {
        super("connect", null, "join");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(new ComponentBuilder(Utils.Prefix + "§cYou need to select a server to connect to.").color(ChatColor.RED).create());
            return;
        }

        ServerInfo server = ProxyServer.getInstance().getServerInfo(args[0]);
        if(server == null) {
            sender.sendMessage(new ComponentBuilder(Utils.Prefix + "§cThe server " + args[0] + " does not exist.").color(ChatColor.RED).create());
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        player.connect(server);
        player.sendMessage(new ComponentBuilder(Utils.Prefix + "§aSending you to " + server.getName() + ".").color(ChatColor.GREEN).create());
    }
}
