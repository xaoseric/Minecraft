package com.fadelands.bungeecore.discord;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DiscordUtils extends ListenerAdapter {

    public static String PREFIX = "!";

    public static TextChannel getDiscordLogs() {
        return BuildBot.jda.getGuildById("402096765018570752").getTextChannelById("402206605262848000");
    }
    public static TextChannel getIngameReports() {
        return BuildBot.jda.getGuildById("402096765018570752").getTextChannelById("455860720425369600");
    }
    public static TextChannel getPunishmentsChannel() {
        return BuildBot.jda.getGuildById("402096765018570752").getTextChannelById("479701389438877710");
    }
    public static TextChannel getServerLogs() {
        return BuildBot.jda.getGuildById("402096765018570752").getTextChannelById("453571821737082889");
    }
}