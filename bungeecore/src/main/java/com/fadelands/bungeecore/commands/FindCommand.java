package com.fadelands.bungeecore.commands;

import com.fadelands.bungeecore.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class FindCommand extends Command {

    public FindCommand() {
        super("find", "core.find", "findplayer", "where");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender.hasPermission("fadelands.find"))){
            commandSender.sendMessage(new ComponentBuilder(Utils.No_Perm).create());
            return;
        }
        if(args.length == 0){
            commandSender.sendMessage(new ComponentBuilder(Utils.Prefix + "§cInvalid usage. Correct usage is /find <target>.").create());
            return;
        }
        String targetStr  = args[0];

        if(ProxyServer.getInstance().getPlayer(targetStr) == null){
            commandSender.sendMessage(new ComponentBuilder(Utils.Prefix + "§cThat is not a valid player.").create());
            return;
        }
        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetStr);

        commandSender.sendMessage(new ComponentBuilder(Utils.Prefix + "§a" + target.getName() + " §2can be found at §a" + target.getServer().getInfo().getName() + "§2.").create());

    }
}