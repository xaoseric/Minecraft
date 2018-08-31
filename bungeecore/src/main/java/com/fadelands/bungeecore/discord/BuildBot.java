package com.fadelands.bungeecore.discord;

import com.fadelands.bungeecore.Main;
import com.fadelands.bungeecore.discord.discordevents.MemberEvents;
import com.fadelands.bungeecore.discord.discordevents.MessageEvents;
import com.fadelands.bungeecore.discord.discordevents.RoleEvents;
import com.fadelands.bungeecore.discord.discordevents.discordcommands.IPCommand;
import com.fadelands.bungeecore.discord.discordevents.discordcommands.LinksCommand;
import com.fadelands.bungeecore.discord.discordevents.discordcommands.ServerStatus;
import com.fadelands.bungeecore.discord.discordevents.discordcommands.ingamereports.DiscordReportInfoCommand;
import com.fadelands.bungeecore.discord.discordsync.MessageOnJoin;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.fadelands.bungeecore.discord.discordevents.discordcommands.SpecificServerStatus;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;
import java.util.logging.Logger;

public class BuildBot {

    private static final Logger log = Logger.getLogger("Minecraft");

    public static JDA jda;

    public static void build() {
        EventWaiter waiter = new EventWaiter();

        CommandClientBuilder client = new CommandClientBuilder();
        client.useDefaultGame();
        client.setOwnerId("161494492422078464");
        client.setEmojis("\uD83D\uDE03", "\uD83D\uDE2E", "\uD83D\uDE26");
        client.setPrefix("!");
        client.setGame(Game.playing("play.fadelands.com"));

        // Add Commands
        client.addCommand(new ServerStatus());
        client.addCommand(new SpecificServerStatus());
        client.addCommand(new IPCommand());
        client.addCommand(new LinksCommand());
        client.addCommand(new DiscordReportInfoCommand());

        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(Main.config.getString("token"));
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setAutoReconnect(true);
        builder.setGame(Game.playing("play.fadelands.com"));

        builder.addEventListener(new MessageEvents());
        builder.addEventListener(new RoleEvents());
        builder.addEventListener(new MemberEvents());
        builder.addEventListener(new MessageOnJoin());

        builder.addEventListener(waiter);
        builder.addEventListener(client.build());

        try {
            BuildBot.jda = builder.buildBlocking();
            log.info("Discord Bot started.");
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

