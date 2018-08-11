package com.fadelands.bungeecore.commands.reports;

import com.fadelands.bungeecore.utils.Utils;
import com.fadelands.bungeecore.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportInfoCommand extends Command {
    public ReportInfoCommand() {
        super("reportinfo", "fadelands.reportinfo", "reviewreport");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage(new ComponentBuilder("This command can only be used ingame.").color(ChatColor.RED).create());
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) commandSender;
        if (!(player.hasPermission("fadelands.reportinfo"))) {
            player.sendMessage(new ComponentBuilder(Utils.No_Perm).color(ChatColor.RED).create());
            return;
        }
        if(args.length == 0){
            player.sendMessage(new ComponentBuilder(Utils.Prefix + "§cInvalid usage. /reportinfo <ID>").color(ChatColor.RED).create());
            return;
        }
        String query = "SELECT * FROM fadelands_ingamereports WHERE report_id='" + args[0] + "'";
        try (Connection connection = Main.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement(query)){
                try(ResultSet rs = statement.executeQuery()) {
                    if (!rs.next()) {
                        player.sendMessage(new ComponentBuilder(Utils.Prefix_Red + "§cI could either not find that report or you may have entered an unvalid ID.").color(ChatColor.RED).create());
                    } else {
                        if (rs.getString("status").equals("UNHANDLED")) {
                            player.sendMessage(new ComponentBuilder(Utils.Prefix + "§3Reviewing Report ID #" + rs.getString("report_id") + ".").color(ChatColor.DARK_AQUA).create());
                            commandSender.sendMessage(new ComponentBuilder("" +
                                    "§8§l┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓" + "\n" +
                                    "§8§l┣ §3§lREPORT #§b§l" + rs.getString("report_id") + " §3§l- §b" + rs.getString("reported_username") + "\n" +
                                    "§8§l┣ §a(" + rs.getString("reported_uuid") + ")" + "\n" +
                                    "§8§l┣ §3Report Reason: §b" + rs.getString("reason") + "\n" +
                                    "§8§l┣ §3Server: §b" + rs.getString("server") + "\n" +
                                    "§8§l┣ §3Date: §b" + rs.getTimestamp("date") + "\n" +
                                    "§8§l┣ §3Reported By: §b" + rs.getString("player_username") + "\n" +
                                    "§8§l┣ §a(" + rs.getString("player_uuid") + ")" + "\n" +
                                    "§8§l┣ §3Status: §b" + rs.getString("status") + "\n" +
                                    "§8§l┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛").create());
                        }
                        if (rs.getString("status").equals("CLOSED")) {
                            player.sendMessage(new ComponentBuilder(Utils.Prefix + "§3Reviewing Report ID #" + rs.getString("report_id") + ".").color(ChatColor.DARK_AQUA).create());
                            commandSender.sendMessage(new ComponentBuilder("" +
                                    "§8§l┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓" + "\n" +
                                    "§8§l┣ §3§lREPORT #§b§l" + rs.getString("report_id") + " §3§l- §b" + rs.getString("reported_username") + "\n" +
                                    "§8§l┣ §a(" + rs.getString("reported_uuid") + ")" + "\n" +
                                    "§8§l┣ §3Report Reason: §b" + rs.getString("reason") + "\n" +
                                    "§8§l┣ §3Server: §b" + rs.getString("server") + "\n" +
                                    "§8§l┣ §3Date: §b" + rs.getTimestamp("date") + "\n" +
                                    "§8§l┣ §3Reported By: §b" + rs.getString("player_username") + "\n" +
                                    "§8§l┣ §a(" + rs.getString("player_uuid") + ")" + "\n" +
                                    "§8§l┣ §3Status: §b" + rs.getString("status") + "\n" +
                                    "§8§l┣ §3Handled By: §b" + rs.getString("handled_by") + "\n" +
                                    "§8§l┣ §3Date handled on: §b" + rs.getString("handled_date") + "\n" +
                                    "§8§l┣ §3Comment: §b" + rs.getString("handled_text") + "\n" +
                                    "§8§l┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛").create());
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
