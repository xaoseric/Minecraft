package com.fadelands.bungeecore.commands.reports;

import com.fadelands.bungeecore.discord.DiscordUtils;
import com.fadelands.bungeecore.utils.DM;
import com.fadelands.bungeecore.Main;
import com.fadelands.bungeecore.MySQL;
import com.fadelands.bungeecore.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HandleReportCommand extends Command {
    public HandleReportCommand() {
        super("handlereport", "fadelands.handlereport", "hreport");
    }

    DM dm = new DM();

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage(new ComponentBuilder("This command can only be used ingame.").color(ChatColor.RED).create());
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) commandSender;
        if (!(player.hasPermission("fadelands.handlereport"))) {
            player.sendMessage(new ComponentBuilder(Utils.No_Perm).color(ChatColor.RED).create());
            return;
        }
        if (args.length == 0) {
            player.sendMessage(new ComponentBuilder(Utils.Prefix + "§cInvalid usage. /handlereport <ID> <COMMENT>").color(ChatColor.RED).create());
            return;
        }
        String query = "SELECT * FROM fadelands_ingamereports WHERE report_id='" + args[0] + "'";
        try (Connection connection = Main.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                try (ResultSet rs = statement.executeQuery()) {
                    if (!rs.next()) {
                        player.sendMessage(new ComponentBuilder(Utils.Prefix_Red + "§cI could either not find that report or you may have entered an unvalid ID.").color(ChatColor.RED).create());
                    } else {
                        if (args.length == 1) {
                            player.sendMessage(new ComponentBuilder(Utils.Prefix + "§cInvalid usage. /handlereport <ID> <COMMENT>").color(ChatColor.RED).create());
                            return;
                        }
                        if (rs.getString("handled_by") != null) {
                            player.sendMessage(new ComponentBuilder(Utils.Prefix_Red + "§cThis report has already been handled.").color(ChatColor.RED).create());
                            return;

                        }
                        StringBuilder sb = new StringBuilder();
                        for (int i = 1; i < args.length; i++)
                            sb.append(args[i] + " ");
                        String comment = sb.toString();

                        Date date = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyy HH:mm:ss");

                        PreparedStatement statement2 = connection.prepareStatement("UPDATE fadelands_ingamereports SET status=?,handled_by=?,handled_uuid=?,handled_date=?,handled_text=? WHERE report_id='" + args[0] + "'");
                        statement2.setString(1, "CLOSED");
                        statement2.setString(2, player.getName());
                        statement2.setString(3, player.getUniqueId().toString());
                        statement2.setString(4, format.format(date));
                        statement2.setString(5, comment);
                        statement2.executeUpdate();
                        statement2.close();

                        player.sendMessage(new ComponentBuilder(Utils.Prefix_Green + "§aHandled report #" + args[0] + " with comment " + comment + ".").color(ChatColor.GREEN).create());
                        DiscordUtils.getIngameReports().sendMessage("Staff member `" + player.getName() + "` handled report `#" + args[0] + "` with comment `" + comment + "`.").queue();

                        // Add to staff data! \\
                        try(PreparedStatement add = connection.prepareStatement("SELECT * FROM fadelands_players_staff WHERE player_uuid='" + player.getUniqueId() + "'")) {
                            try (ResultSet rs2 = add.executeQuery()) {
                                if (rs2.next()) {
                                    MySQL.updateTable(player, "fadelands_players_staff", "reports_handled", rs2.getInt("reports_handled") + 1);
                                    //Added the stats to the staff profile!\\
                                }
                            }
                        }
                    }
                }
            }

                } catch (SQLException e) {
                    e.printStackTrace();

                }

            }
        }

