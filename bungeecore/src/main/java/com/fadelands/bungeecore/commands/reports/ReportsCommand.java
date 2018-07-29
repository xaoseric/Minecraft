package com.fadelands.bungeecore.commands.reports;

import com.fadelands.bungeecore.Utils;
import com.fadelands.bungeecore.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportsCommand extends Command {

    public ReportsCommand() {
        super("reports", "core.reports", "listreports", "activereports");
    }


    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if (!(commandSender instanceof ProxiedPlayer)) {
            commandSender.sendMessage(new ComponentBuilder("This command can only be used ingame.").color(ChatColor.RED).create());
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) commandSender;

        if (!(player.hasPermission("fadelands.reports"))) {
            player.sendMessage(new ComponentBuilder(Utils.No_Perm).color(ChatColor.RED).create());
            return;
        }

        if (args.length == 0) {
            player.sendMessage(new ComponentBuilder(Utils.Prefix + "§cInvalid usage. /reports <UNHANDLED/CLOSED>").color(ChatColor.RED).create());
            return;
        }

        if (args[0].equalsIgnoreCase("unhandled")) {

            final List<String> unhandled = new ArrayList<>();

            try (Connection connection = Main.getConnection()){
                try(ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM fadelands_ingamereports WHERE status='UNHANDLED' ORDER BY report_id")) {

                    while (rs.next()) {
                        unhandled.add("§7#§b" + rs.getInt("report_id") + "§7: §b" + rs.getString("reported_username") + "§7 - §b" + rs.getString("reason") + "§7 - §b" + rs.getString("status"));
                    }
                }
            }catch (SQLException e) {
                e.printStackTrace();
                player.sendMessage(new ComponentBuilder(Utils.Prefix_Red + "§cSomething went terribly wrong. (SQLException)").color(ChatColor.RED).create());
            }

            if (unhandled.size() == 0) {
                player.sendMessage(new ComponentBuilder(Utils.Prefix_Red + "§cThere is no unhandled reports to display.").color(ChatColor.RED).create());
                return;
            }

            StringBuilder builder = new StringBuilder("§8§l§m-]---§3§l #§7§lID §8- §3§lPLAYER §8- §3§lREASON §8- §3§lSTATUS §8§l§m---[-");
            for (int i = 0; i < unhandled.size(); i++) {
                builder.append("\n" + unhandled.get(i));
            }

            player.sendMessage(new ComponentBuilder(builder.toString()).color(ChatColor.DARK_AQUA).create());
        }
    }
}

