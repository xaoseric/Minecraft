package com.fadelands.bungeecore.discordevents.discordcommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

public class LinksCommand extends Command {

    public LinksCommand() {
        this.name = "links";
        this.guildOnly = true;
        this.help = "Show server links.";
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getArgs().split(" ");
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();

        channel.sendMessage("Website: https://fadelands.com \n" +
                "Forums: https://fadelands.com/forums \n" +
                "IP: play/mc.fadelands.com").queue();

    }
}
