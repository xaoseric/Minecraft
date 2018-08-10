package com.fadelands.bungeecore.commands.servers;

import com.fadelands.bungeecore.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CurrentServerCommand  extends Command {

    public CurrentServerCommand(){
        super("server", null, "whereami", "servername");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        ProxiedPlayer player = (ProxiedPlayer) commandSender;
        player.sendMessage(new ComponentBuilder(Utils.Prefix + "§2You are currently on §a" + player.getServer().getInfo().getName() + "§2.").color(ChatColor.GREEN).create());
    }
}

