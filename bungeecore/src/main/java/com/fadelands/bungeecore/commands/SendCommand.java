package com.fadelands.bungeecore.commands;

import com.fadelands.bungeecore.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SendCommand extends Command {

    public SendCommand() {
        super("send", "fadelands.send");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        ProxiedPlayer player = (ProxiedPlayer) commandSender;

        if(args.length == 0) {
            player.sendMessage(new ComponentBuilder(Utils.Prefix + "§cInvalid usage. /send [player] [server]").color(ChatColor.RED).create());
            return;
        }

        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
        if(target == null) {
            player.sendMessage(new ComponentBuilder(Utils.Prefix + "§cCouldn't find that player.").color(ChatColor.RED).create());
            return;
        }

        if(args.length == 1) {
            player.sendMessage(new ComponentBuilder(Utils.Prefix + "§cYou need to choose a server to send them to.").color(ChatColor.RED).create());
            return;
        }

        String srv = args[1];
        ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(srv);
        if(serverInfo == null) {
            player.sendMessage(new ComponentBuilder(Utils.Prefix + "§cThat's not a valid server.").color(ChatColor.RED).create());
            return;
        }

        target.connect(serverInfo);
        target.sendMessage(new ComponentBuilder(Utils.Prefix + "§aYou were sent to " + serverInfo.getName() + " by " + player.getName() + ".").color(ChatColor.GREEN).create());
        player.sendMessage(new ComponentBuilder(Utils.Prefix + "§aYou sent " + target.getName() + " to " + serverInfo.getName() + ".").color(ChatColor.GREEN).create());
    }
}