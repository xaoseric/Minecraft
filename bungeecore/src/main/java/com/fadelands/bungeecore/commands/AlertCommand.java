package com.fadelands.bungeecore.commands;

import com.fadelands.bungeecore.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.stream.Collectors;

public class AlertCommand extends Command {

    public AlertCommand(){
        super("alert", "core.alert", "networkbroadcast", "nbc");
    }
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender.hasPermission("fadelands.alert"))) {
            commandSender.sendMessage(Utils.No_Perm);
            return;
        }
        if (args.length == 0) {
            commandSender.sendMessage(Utils.Prefix + "§cInvalid usage. Correct usage is /alert <text>.");
            return;
        }
        String reasonRaw = Arrays.stream(args).skip(0).collect(Collectors.joining(" "));
        String reason = ChatColor.translateAlternateColorCodes('&', reasonRaw);
        ProxyServer.getInstance().broadcast(new ComponentBuilder("§8│ §3§lAnnouncement§7 »§r " + reason).create());

    }
}
