package com.fadelands.bungeecore.commands.servers;

import com.fadelands.bungeecore.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class SurvivalCommand extends Command {

    public SurvivalCommand() {
        super("survival", null, "sv");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        ProxiedPlayer player = (ProxiedPlayer) commandSender;

        if (strings.length == 0) {
            player.sendMessage(new ComponentBuilder(Utils.Prefix_Red + "§cYou have to specify a Survival Realm! \n  §6Available Realms: None").color(ChatColor.GOLD).create());
            return;
        }
    }
}
