package com.fadelands.bungeecore.commands.reports;

import com.fadelands.bungeecore.Main;
import com.fadelands.bungeecore.discord.DiscordUtils;
import com.fadelands.bungeecore.utils.Perms;
import com.fadelands.bungeecore.utils.Utils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ReportCommand extends Command {

    private final ArrayList<ProxiedPlayer> cooldown = new ArrayList<>();

    public Main plugin;
    public ReportCommand(Main plugin) {
        super("report", null, "reportplayer", "rp", "ezreport", "shitstaff");
        this.plugin = plugin;

    }
    @Override
    public void execute(CommandSender commandSender, String[] args) {
        ProxiedPlayer sender = (ProxiedPlayer) commandSender;
        if(cooldown.contains(sender)){
            sender.sendMessage(new ComponentBuilder(Utils.Prefix + "§cYou have to wait a minute until you can use this command again.").color(ChatColor.RED).create());
            return;
        }
        if (args.length == 0) {
            sender.sendMessage(new ComponentBuilder(Utils.Prefix + "§cInvalid usage! Correct usage is /report <target> <reason>.").color(ChatColor.RED).create());
            return;
        }
        String targetStr = args[0];

        if (ProxyServer.getInstance().getPlayer(targetStr) == null) {
            sender.sendMessage(new ComponentBuilder(Utils.Prefix + "§cThat player is not online.").create());
            return;
        }
        /* todo: Add back after testing.
        if (ProxyServer.getInstance().getPlayer(targetStr) == commandSender) {
            sender.sendMessage(new ComponentBuilder(Utils.Prefix + "§cYou can't report yourself.").create());
            return;
        }*/

        sender.sendMessage(new ComponentBuilder(Utils.Prefix_Green + "§aThank you, your report has been sent. A staff member will deal with it as soon as possible.").color(ChatColor.GREEN).create());
        sender.sendMessage(new ComponentBuilder("§4§l(!) §cKeep in mind, abusing this command will result in a punishment.").color(ChatColor.RED).create());
        cooldown.add(sender);
        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> cooldown.remove(sender), 60, TimeUnit.SECONDS);

        for (ProxiedPlayer staff : ProxyServer.getInstance().getPlayers()) {
            if (staff.hasPermission(Perms.staffAlerts)) {

                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetStr);
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < args.length; i++)
                    sb.append(args[i]).append(" ");
                String reason = sb.toString();

                // Attempting to enter the report info to the db. \\

                Connection connection = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                int id = 0;

                try {
                    connection = Main.getConnection();
                    ps = connection.prepareStatement("INSERT INTO " +
                            "reports " +
                            "(" +
                            "reporter," +
                            "reported," +
                            "server," +
                            "reason," +
                            "date," +
                            "status," +
                            "staff," +
                            "handled," +
                            "comment" +
                            ") " +
                            "VALUE (?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, sender.getUniqueId().toString());
                    ps.setString(2, target.getUniqueId().toString());
                    ps.setString(3, target.getServer().getInfo().getName());
                    ps.setString(4, reason);
                    ps.setTimestamp(5, new Timestamp(new DateTime(DateTimeZone.UTC).getMillis()));
                    ps.setInt(6, ReportStatusType.OPEN.ordinal());
                    ps.setString(7, null);
                    ps.setTimestamp(8, null);
                    ps.setString(9, null);
                    ps.executeUpdate();

                   // Getting the results from the latest insert.

                    rs = ps.getGeneratedKeys();
                        if (rs.next()) {
                            id = rs.getInt(1);
                            // Inserted data to db, starting messaging process \\
                            staff.sendMessage(new ComponentBuilder("" +
                                    "§8§l┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓" + "\n" +
                                    "§8§l┣ §c§lNew Report - §6ID: #" + id + "\n" +
                                    "§8§l┣ §2Reported: §e" + target.getName() + "\n" +
                                    "§8§l┣ §2Reason: §e" + (reason.equals("") ? "No Reason specified." : reason) + "\n" +
                                    "§8§l┣ §2Report By: §e" + sender.getName() + "\n" +
                                    "§8§l┣ §2Server: §e" + target.getServer().getInfo().getName() + "\n" +
                                    "§8§l┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛")
                                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Press to connect to server.").color(ChatColor.GOLD)
                                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "connect " + target.getServer().getInfo().getName())).create())).create());

                            // Sending an embed message to discord to alert staff members that aren't on the server. \\
                            EmbedBuilder embed = new EmbedBuilder();
                            embed.setTitle("New In-game Report");
                            embed.setColor(Color.decode("#5c85d6"));
                            embed.setDescription("Reported User: **" + target.getName() + "** `(" + target.getUniqueId().toString() + ")` \n" +
                                    "Reported By: **" + sender.getName() + "** `(" + sender.getUniqueId().toString() + ")` \n" +
                                    "Reason: **" + reason + "** \n" +
                                    "Server: **" + target.getServer().getInfo().getName() + "** \n" +
                                    "ID: **" + id + "**");

                            embed.setFooter("For more info: /reportinfo " + id + " | To handle report: /handlereport " + id, null);

                            MessageEmbed build = embed.build();

                            DiscordUtils.getIngameReports().sendMessage(build).queue();
                            rs.close();
                        }
                    } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    Main.closeComponents(rs, ps, connection);
                }
            }
        }
    }
}
