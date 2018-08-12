package com.fadelands.bungeecore.commands.reports;

import com.fadelands.bungeecore.Main;
import com.fadelands.bungeecore.utils.Perms;
import com.fadelands.bungeecore.utils.Utils;
import com.fadelands.bungeecore.discordevents.DiscordUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
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
            sender.sendMessage(new ComponentBuilder(Utils.Prefix_Red + "§cYou have to wait a minute until you can use this command again.").color(ChatColor.RED).create());
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
        if (ProxyServer.getInstance().getPlayer(targetStr) == commandSender) {
            sender.sendMessage(new ComponentBuilder(Utils.Prefix + "§cYou can't report yourself.").create());
            return;
        }

        sender.sendMessage(new ComponentBuilder(Utils.Prefix_Green + "§aThank you, your report has been sent. A staff member will deal with it as soon as possible.").color(ChatColor.GREEN).create());
        sender.sendMessage(new ComponentBuilder("§4§l(!) §cKeep in mind, abusing this command will result in a punishments.").color(ChatColor.RED).create());
        cooldown.add(sender);
        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> cooldown.remove(sender), 60, TimeUnit.SECONDS);

        for (ProxiedPlayer staff : ProxyServer.getInstance().getPlayers()) {
            if (staff.hasPermission(Perms.staffAlerts)) {

                ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetStr);
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < args.length; i++)
                    sb.append(args[i] + " ");
                String reason = sb.toString();

                // Attempting to enter the report info to the db. \\

                try (Connection connection = Main.getConnection()){
                    try(PreparedStatement insert = connection.prepareStatement("INSERT INTO " +
                            "fadelands_ingamereports " +
                            "(" +
                            "player_uuid," +
                            "player_username," +
                            "reported_uuid," +
                            "reported_username," +
                            "server," +
                            "date," +
                            "reason," +
                            "status," +
                            "handled_by," +
                            "handled_uuid," +
                            "handled_date," +
                            "handled_text" +
                            ") " +
                            "VALUE (?,?,?,?,?,?,?,?,?,?,?,?)")) {

                        insert.setString(1, sender.getUniqueId().toString());
                        insert.setString(2, sender.getName());
                        insert.setString(3, target.getUniqueId().toString());
                        insert.setString(4, target.getName());
                        insert.setString(5, target.getServer().getInfo().getName());
                        insert.setTimestamp(6, new Timestamp(new DateTime(DateTimeZone.UTC).getMillis()));
                        insert.setString(7, reason);
                        insert.setString(8, "UNHANDLED");
                        insert.setString(9, null);
                        insert.setString(10, null);
                        insert.setString(11, null);
                        insert.setString(12, null);
                        insert.executeUpdate();
                        insert.close();

                        // Getting the results from the latest insert. \\

                        ResultSet rs = connection.createStatement().executeQuery("SELECT SCOPE_IDENTITY()");
                        if (rs.next()) {
                            // Inserted data to db, starting to messaging process \\
                            staff.sendMessage(new ComponentBuilder("" +
                                    "§8§l┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓" + "\n" +
                                    "§8§l┣ §c§lNew In-game Report §cID: #" + rs.getString("LAST_INSERTED_ID()") + "\n" +
                                    "§8§l┣ §3Reported: §b" + target.getName() + "\n" +
                                    "§8§l┣ §3Reason: §b" + (reason.equals("") ? "No Reason specified." : reason) + "\n" +
                                    "§8§l┣ §3Report By: §b" + sender.getName() + "\n" +
                                    "§8§l┣ §3Server: §b" + target.getServer().getInfo().getName() + "\n" +
                                    "§8§l┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛").create());


                            // Sending an embed message to discord to alert staff members that aren't on the server. \\
                            EmbedBuilder embed = new EmbedBuilder();
                            embed.setTitle("New In-game Report");
                            embed.setColor(Color.darkGray);

                            embed.setDescription("Player `" + target.getName() +
                                    " (" + target.getUniqueId().toString() + ")` has been reported for `" + reason +
                                    "` by player `" + sender.getName() +
                                    " (" + sender.getUniqueId().toString() + ")`\n" +
                                    "**Server:** `" + target.getServer().getInfo().getName() + "` \n" +
                                    "**Report ID:** `#" + rs.getString("report_id") + "`");

                            embed.setFooter("For more info: /reportinfo " + rs.getString("report_id") + " | To handle report: /handlereport " + rs.getString("report_id"), null);

                            MessageEmbed build = embed.build();

                            DiscordUtils.getIngameReports().sendMessage(build).queue();
                            rs.close();
                        }
                    }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
            }
        }
    }
}
