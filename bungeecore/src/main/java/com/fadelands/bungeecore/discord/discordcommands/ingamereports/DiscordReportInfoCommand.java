package com.fadelands.bungeecore.discord.discordevents.discordcommands.ingamereports;


import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.fadelands.bungeecore.Main;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DiscordReportInfoCommand extends Command {

    public DiscordReportInfoCommand() {
        this.name = "reportinfo";
        this.guildOnly = true;
        this.help = "Shows info of a ingame report.";
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getArgs().split(" ");
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();

        Member member = event.getMember();
        if (!(member.hasPermission(Permission.BAN_MEMBERS))) {
           channel.sendMessage("You don't have permission to do that, " + member.getAsMention()).queue();
            return;

        }
        String query = "SELECT * FROM ingamereports WHERE report_id='" + args[0] + "'";
        try(Connection connection = Main.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement(query)){
                try(ResultSet rs = statement.executeQuery()) {
                    if (!rs.next()) {
                        channel.sendMessage(member.getAsMention() + " I could either not find that report or you may have entered an unvalid ID.").queue();
                    } else {
                        if (rs.getString("status").equals("UNHANDLED")) {
                            EmbedBuilder embed = new EmbedBuilder();
                            embed.setTitle("Reviewing Report ID #" + rs.getString("report_id") + " | " + rs.getString("reported_username"));

                            embed.setColor(new Color(54, 57, 62));

                            embed.setDescription("Report Reason: `" + rs.getString("reason") + "`\n" +
                                    "Server: `" + rs.getString("server") + "`\n" +
                                    "Date: `" + rs.getString("date") + "`\n" +
                                    "Reported By: `" + rs.getString("player_username") + "`\n" +
                                    "Status: `" + rs.getString("status") + "`");
                            MessageEmbed build = embed.build();
                            channel.sendMessage(build).queue();
                        }
                        if (rs.getString("status").equals("CLOSED")) {
                            EmbedBuilder embed = new EmbedBuilder();
                            embed.setTitle("Reviewing Report ID #" + rs.getString("report_id") + " | " + rs.getString("reported_username"));

                            embed.setColor(new Color(54, 57, 62));

                            embed.setDescription("Report Reason: `" + rs.getString("reason") + "`\n" +
                                    "Server: `" + rs.getString("server") + "`\n" +
                                    "Date: `" + rs.getString("date") + "`\n" +
                                    "Reported By: `" + rs.getString("player_username") + "`\n" +
                                    "Status: `" + rs.getString("status") + "`\n" +
                                    "Handled By: `" + rs.getString("handled_by") + "`\n" +
                                    "Date Handled On: `" + rs.getString("handled_date") + "`\n" +
                                    "Comment from Handler: `" + rs.getString("handled_text") + "`");

                            MessageEmbed build = embed.build();
                            channel.sendMessage(build).queue();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
