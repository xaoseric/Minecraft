package com.fadelands.bungeecore.discordevents.discordcommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class SpecificServerStatus extends Command {

    public SpecificServerStatus() {
        this.name = "server";
        this.guildOnly = true;
        this.aliases = new String[]{"serverinfo"};
        this.help = "Show the server info.";
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getArgs().split(" ");
        MessageChannel channel = event.getChannel();
        if (args.length <= 0) {
            channel.sendMessage("You must enter a server name. Example: !server HUB").complete().delete().queueAfter(15, TimeUnit.SECONDS);
            return;
        }
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            channel.sendMessage("You cant use this command.").queue();
            return;
        }
        String serverStr = args[0];
        if (ProxyServer.getInstance().getServerInfo(serverStr) == null) {
            channel.sendMessage("The server " + args[0] + " does not exist in the Bungee Cord configuration.").queue();
            return;
        }

        ServerInfo server = ProxyServer.getInstance().getServerInfo(args[0]);

        server.ping(((serverPing, throwable) -> {
            if (throwable == null) {
                EmbedBuilder embedOnline = new EmbedBuilder();
                embedOnline.setTitle("Server Status | " + server.getName());
                embedOnline.setColor(Color.green);

                embedOnline.addField("Status", "ONLINE", true);
                embedOnline.addField("Players Online", String.valueOf(server.getPlayers().size()), true);
                embedOnline.addField("Max Slots", String.valueOf(serverPing.getPlayers().getMax()), true);
                embedOnline.addField("MOTD", server.getMotd().replaceAll("§", "c:"), true);

                MessageEmbed buildOnline = embedOnline.build();
                channel.sendMessage(buildOnline).queue();
            } else {
                EmbedBuilder embedOffline = new EmbedBuilder();
                embedOffline.setTitle("Server Status | " + server.getName());
                embedOffline.setColor(Color.red);

                embedOffline.setDescription(server.getName() + " is currently **OFFLINE**.");

                MessageEmbed buildOffline = embedOffline.build();
                channel.sendMessage(buildOffline).queue();
            }
        }));
    }
}
