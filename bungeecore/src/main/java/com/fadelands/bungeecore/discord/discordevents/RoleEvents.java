package com.fadelands.bungeecore.discord.discordevents;

import com.fadelands.bungeecore.discord.DiscordUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.role.RoleCreateEvent;
import net.dv8tion.jda.core.events.role.RoleDeleteEvent;
import net.dv8tion.jda.core.events.role.update.RoleUpdateNameEvent;
import net.dv8tion.jda.core.events.role.update.RoleUpdatePermissionsEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.time.Instant;
import java.util.stream.Collectors;

public class RoleEvents extends ListenerAdapter {

    @Override
    public void onRoleUpdatePermissions(RoleUpdatePermissionsEvent event) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("A Role was updated.");
        embed.setColor(Color.darkGray);
        embed.setThumbnail(null);
        embed.setTimestamp(Instant.now());
        embed.setTimestamp(null);

        embed.addField("Role", event.getRole().getName() + " (" + event.getRole().getId() + ") " + event.getRole().getAsMention(), false);
        embed.addField("Old Permissions", event.getOldPermissions().stream().map(Permission::getName).collect(Collectors.joining(", ")), true);
        embed.addField("New Permissions", event.getNewPermissions().stream().map(Permission::getName).collect(Collectors.joining(", ")), true);
        MessageEmbed build = embed.build();

        DiscordUtils.getDiscordLogs().sendMessage(build).queue();
        embed.clearFields();
    }

    @Override
    public void onRoleDelete(RoleDeleteEvent event) {

        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("A Role was deleted.");
        embed.setColor(Color.darkGray);
        embed.setThumbnail(null);
        embed.setTimestamp(Instant.now());
        embed.setTimestamp(null);

        embed.addField("Role", event.getRole().getName() + " (" + event.getRole().getId() + ")", false);

        MessageEmbed build = embed.build();

        DiscordUtils.getDiscordLogs().sendMessage(build).queue();
        embed.clearFields();
    }

    @Override
    public void onRoleCreate(RoleCreateEvent event) {

        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("A Role was created.");
        embed.setColor(Color.darkGray);
        embed.setThumbnail(null);
        embed.setTimestamp(Instant.now());
        embed.setTimestamp(null);

        embed.addField("Role", event.getRole().getName() + " (" + event.getRole().getId() + ") " + event.getRole().getAsMention(), false);
        embed.addField("Permissions", event.getRole().getPermissions().stream().map(Permission::getName).collect(Collectors.joining(", ")), false);

        MessageEmbed build = embed.build();

        DiscordUtils.getDiscordLogs().sendMessage(build).queue();
        embed.clearFields();
    }

    @Override
    public void onRoleUpdateName(RoleUpdateNameEvent event) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("A Role was updated.");
        embed.setColor(Color.darkGray);
        embed.setThumbnail(null);
        embed.setTimestamp(Instant.now());
        embed.setTimestamp(null);

        embed.addField("Role", event.getRole().getName() + " (" + event.getRole().getId() + ") " + event.getRole().getAsMention(), false);
        embed.addField("Old Name", event.getOldName(), true);
        embed.addField("New Name", event.getNewName(), true);

        MessageEmbed build = embed.build();

        DiscordUtils.getDiscordLogs().sendMessage(build).queue();
        embed.clearFields();
    }
}
