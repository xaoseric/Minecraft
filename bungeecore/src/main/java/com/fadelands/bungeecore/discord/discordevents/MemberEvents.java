package com.fadelands.bungeecore.discord.discordevents;

import com.fadelands.bungeecore.discord.DiscordUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.IMentionable;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.time.Instant;
import java.util.stream.Collectors;

public class MemberEvents extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("Member joined the guild.");
        embed.setColor(Color.darkGray);
        embed.setFooter("A member joined the guild", null);
        embed.setThumbnail(event.getMember().getUser().getEffectiveAvatarUrl());

        embed.addField("New Member", event.getMember().getUser().getName() + " - " + event.getMember().getAsMention(), true);

        MessageEmbed build = embed.build();

        DiscordUtils.getDiscordLogs().sendMessage(build).queue();
        embed.clearFields();
    }

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("Member left the guild");
        embed.setColor(Color.darkGray);
        embed.setFooter("A member left the guild.", null);
        embed.setThumbnail(event.getMember().getUser().getEffectiveAvatarUrl());

        embed.addField("Member", event.getMember().getUser().getName() + " - " + event.getMember().getAsMention(), true);

        MessageEmbed build = embed.build();

        DiscordUtils.getDiscordLogs().sendMessage(build).queue();
        embed.clearFields();
    }
    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("Members roles was updated");
        embed.setFooter("A members roles was updated.", null);
        embed.setColor(Color.darkGray);
        embed.setThumbnail(null);
        embed.setTimestamp(Instant.now());
        embed.setTimestamp(null);

        embed.addField("User", event.getMember().getUser().getName() + " - " + event.getMember().getUser().getId() + " - " + event.getMember().getAsMention(), false);
        embed.addField("Added Role(s)", event.getRoles().stream().map(IMentionable::getAsMention).collect(Collectors.joining(" ")), false);

        MessageEmbed build = embed.build();

        DiscordUtils.getDiscordLogs().sendMessage(build).queue();
        embed.clearFields();
    }
    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("Member Roles updated");
        embed.setFooter("A members roles was updated.", null);
        embed.setColor(Color.darkGray);
        embed.setThumbnail(null);
        embed.setTimestamp(Instant.now());
        embed.setTimestamp(null);

        embed.addField("User", event.getMember().getUser().getName() + " - " + event.getMember().getUser().getId() + " - " + event.getMember().getAsMention(), false);
        embed.addField("Removed Role(s)", event.getRoles().stream().map(IMentionable::getAsMention).collect(Collectors.joining(" ")), false);

        MessageEmbed build = embed.build();

       DiscordUtils.getDiscordLogs().sendMessage(build).queue();
        embed.clearFields();

    }

}

