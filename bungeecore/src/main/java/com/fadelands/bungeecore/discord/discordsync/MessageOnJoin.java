package com.fadelands.bungeecore.discord.discordsync;

import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageOnJoin extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        User user = event.getMember().getUser();
        user.openPrivateChannel().queue((channel) ->

                channel.sendMessage("**Welcome to FadeLands Official Discord!** \n" +
                        " \n" +
                        "To register your Minecraft account with your in-game account please use the following command in game `/discord link " + user.getId() + "`. \n**Do not share the ID to anyone!** \n" +
                        "\nYou can unlink your account with `/discord unlink` or check the account you are currently linked to with `/discord`. \n" +
                        "\nOnce again, welcome to our Discord!").queue());
    }
}