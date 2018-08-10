package com.fadelands.bungeecore.commands;

import com.fadelands.bungeecore.utils.Perms;
import com.fadelands.bungeecore.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SCCommand extends Command {

    public SCCommand(){
        super("modchat", "core.modchat", "mc");
    }
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender.hasPermission("fadelands.modchat"))){
            commandSender.sendMessage(new ComponentBuilder(Utils.No_Perm).color(ChatColor.RED).create());
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) commandSender;
        if(args.length == 0){
            commandSender.sendMessage(new ComponentBuilder(Utils.Prefix + "§cYou have to enter a message.").color(ChatColor.RED).create());
            return;
        }
        String message = Arrays.stream(args).skip(0).collect(Collectors.joining(" "));

        for(ProxiedPlayer staff : ProxyServer.getInstance().getPlayers()){
            if(staff.hasPermission(Perms.staffAlerts)){
                staff.sendMessage(new ComponentBuilder("§3[" + player.getServer().getInfo().getName() + "] §b" + player.getName() + ": " + message).color(ChatColor.AQUA).create());
            }
        }

    }
}
