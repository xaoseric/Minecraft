package com.fadelands.bungeecore.discordevents.discordcommands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

public class IPCommand extends Command {

    public IPCommand() {
        this.name = "ip";
        this.guildOnly = true;
        this.help = "Shows the IP of the server.";
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getArgs().split(" ");
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();

        channel.sendMessage("Server IP: `play.fadelands.com`").queue();

    }
}