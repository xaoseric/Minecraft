package com.fadelands.bungeecore.discordevents;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageDeleteEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class DCMessageEvents extends ListenerAdapter {

    private ExpiringMap<String, Message> messages;

    public DCMessageEvents() {
        this.messages = ExpiringMap.builder().variableExpiration().build();

    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getGuild() == null || event.getMember() == null)
            return;
        messages.put(event.getGuild().getId() + "|" + event.getMessage().getId(), event.getMessage(), ExpirationPolicy.CREATED, 10, TimeUnit.MINUTES);
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent event) {
        if (event.getGuild() == null) return;
        Message old = messages.get(event.getGuild().getId() + "|" + event.getMessageId());
        Message newMessage = event.getMessage();
        if (old != null && !old.getContentDisplay().equals("")) {
            messages.remove(event.getGuild().getId() + "|" + event.getMessageId());
            messages.put(event.getGuild().getId() + "|" + event.getMessageId(), event.getMessage(), ExpirationPolicy.CREATED, 15, TimeUnit.MINUTES);
        }
        if (event.getMessage().isEdited()) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("A message has been edited.");
            embed.setColor(Color.darkGray);
            embed.setThumbnail(null);
            embed.setTimestamp(Instant.now());
            embed.setTimestamp(null);

            embed.addField("Message from", event.getMember().getUser().getAsMention(), true);
            embed.addField("Text Channel", event.getTextChannel().getAsMention(), true);
            embed.addField("First Message", "`" + old.getContentRaw() + "`", false);
            embed.addField("New Message", "`" + newMessage.getContentRaw() + "`", false);

            MessageEmbed build = embed.build();

            DiscordUtils.getDiscordLogs().sendMessage(build).queue();
            embed.clearFields();
        }
    }
    @Override
    public void onMessageDelete(MessageDeleteEvent event) {
        if (event.getGuild() == null) return;
        Message message = messages.get(event.getGuild().getId() + "|" + event.getMessageId());
        Member member = event.getGuild().getMember(message.getAuthor());
        if (message != null && !message.getContentDisplay().equals("")) {

            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("A message has been deleted.");
            embed.setFooter("A message has been deleted.", null);
            embed.setColor(Color.darkGray);
            embed.setThumbnail(null);
            embed.setTimestamp(Instant.now());
            embed.setTimestamp(null);

            embed.addField("Member", member.getUser().getAsMention(), true);
            embed.addField("Text Channel", event.getTextChannel().getAsMention(), true);
            embed.addField("Message", "`" + message.getContentRaw() + "`", false);
            MessageEmbed build = embed.build();

            DiscordUtils.getDiscordLogs().sendMessage(build).queue();
            embed.clearFields();
        }
    }
}
