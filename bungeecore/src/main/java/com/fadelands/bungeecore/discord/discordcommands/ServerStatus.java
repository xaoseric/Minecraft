package com.fadelands.bungeecore.discord.discordevents.discordcommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.fadelands.bungeecore.utils.GetServerStatus;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;


public class ServerStatus extends Command {

    private final EmbedBuilder embed = new EmbedBuilder();

    public ServerStatus() {
        this.name = "status";
        this.guildOnly = true;
        this.aliases = new String[]{"serverstatus", "ss"};
        this.help = "Show the server status.";

    }

    @Override
    protected void execute(CommandEvent event) {
        MessageChannel channel = event.getChannel();

        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("Server Status");
        embed.setColor(Color.darkGray);

        embed.addField("Lobby", GetServerStatus.getStatus("hub").name(), true);
        embed.addField("SkyBlock", GetServerStatus.getStatus("skyblock").name(), true);
        embed.addField("Factions", GetServerStatus.getStatus("factions").name(), true);

        MessageEmbed build = embed.build();
        channel.sendMessage(build).queue();

    }
}
