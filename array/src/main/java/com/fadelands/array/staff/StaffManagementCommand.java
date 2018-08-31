
/**
 * I'm changing this to spigot when I feel motivated enough :]
 **/

/*

package me.arrayofc.Array.staffmanagement;


import org.bukkit.command.CommandExecutor;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.database.Connection;
import java.database.PreparedStatement;
import java.database.ResultSet;
import java.database.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StaffManagementCommand implements CommandExecutor {

    public StaffManagementCommand() {
        super("staff", "core.staffmanagement");
    }

    DM dm = new DM();

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!(commandSender.hasPermission("fadelands.staffmanagement"))) {
            commandSender.sendMessage(new ComponentBuilder(Utils.No_Perm).color(ChatColor.RED).create());
            return;
        }

        if (args.length == 0) {
            commandSender.sendMessage(new ComponentBuilder(Utils.Prefix + "§cInvalid usage. /staff <ADD/DELETE/VIEW/UPDATENAME> <TARGET>").color(ChatColor.RED).create());
            return;
        }

        if (args[0].equalsIgnoreCase("add")) {
            if (!(commandSender.hasPermission("fadelands.staffmanagement.add"))) {
                commandSender.sendMessage(new ComponentBuilder(Utils.Prefix_Red + "§cYou don't have permission to use that subcommand.").color(ChatColor.RED).create());
                return;
            }
            if(args.length == 1){
                commandSender.sendMessage(new ComponentBuilder(Utils.Prefix + "§cPlease enter a valid user.").color(ChatColor.RED).create());
                return;
            }
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
            if (target == null) {
                commandSender.sendMessage(new ComponentBuilder(Utils.Prefix_Red + "§cThat's not a valid user.").color(ChatColor.RED).create());
                return;
            }
            try(Connection connection = Main.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM fadelands_players_staff WHERE player_uuid = ?")) {
                    statement.setString(1, target.getUniqueId().toString());
                    try (ResultSet rs = statement.executeQuery()) {
                        boolean exists = rs.next();

                        if (exists) {
                            commandSender.sendMessage(new ComponentBuilder(Utils.Prefix_Red + "§cThat user already exists in the staff database.").color(ChatColor.RED).create());
                            return;
                        } else {

                        }
                        try (PreparedStatement insert = connection.prepareStatement("INSERT INTO " +
                                "fadelands_players_staff " +
                                "(" +
                                "player_uuid," +
                                "player_username," +
                                "date_hired," +
                                "bans," +
                                "kicks," +
                                "mutes," +
                                "reports_handled" +
                                ") " +
                                "VALUE (?,?,?,?,?,?,?)")) {

                            Date date = new Date();
                            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                            insert.setString(1, target.getUniqueId().toString());
                            insert.setString(2, target.getName());
                            insert.setTimestamp(3, new java.database.Timestamp(new DateTime(DateTimeZone.UTC).getMillis()));
                            insert.setInt(4, 0);
                            insert.setInt(5, 0);
                            insert.setInt(6, 0);
                            insert.setInt(7, 0);
                            insert.executeUpdate();
                            insert.close();
                            dm.log("User `" + commandSender.getName() + "` added a new staff member to the database: `" + target.getName() + " (" + target.getUniqueId().toString() + ")" + "`.");
                            commandSender.sendMessage(new ComponentBuilder(Utils.Prefix_Green + "§aSuccessfully added " + target.getName() + " to staff database.").color(ChatColor.GREEN).create());
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                dm.warning("A SQL exception occurred in **" + this.getClass().getName() + "**, at line **" + new Exception().getStackTrace()[0].getLineNumber() + "**. \nStack trace: `" + e.getMessage() + "` - " + BuildBot.jda.getUserById("161494492422078464").getAsMention());

            }
        }
        if (args[0].equalsIgnoreCase("delete")) {
            if (!(commandSender.hasPermission("fadelands.staffmanagement.delete"))) {
                commandSender.sendMessage(new ComponentBuilder(Utils.Prefix_Red + "§cYou don't have permission to use that subcommand.").color(ChatColor.RED).create());
                return;
            }
            if(args.length == 1){
                commandSender.sendMessage(new ComponentBuilder(Utils.Prefix + "§cPlease enter a valid user.").color(ChatColor.RED).create());
                return;
            }
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[1]);
            if (target == null) {
                commandSender.sendMessage(new ComponentBuilder(Utils.Prefix_Red + "§cThat's not a valid user.").color(ChatColor.RED).create());
                return;
            }
            try (Connection connection = Main.getConnection()){
                try(PreparedStatement statement = connection.prepareStatement("SELECT * FROM fadelands_players_staff WHERE player_uuid = ?")) {
                    statement.setString(1, target.getUniqueId().toString());
                    try (ResultSet rs = statement.executeQuery()) {
                        boolean exists = rs.next();

                        if (!exists) {
                            commandSender.sendMessage(new ComponentBuilder(Utils.Prefix_Red + "§cThere is no user with that name in the staff database.").color(ChatColor.RED).create());
                            return;
                        } else {

                        }

                        PreparedStatement insert = connection.prepareStatement("DELETE FROM fadelands_players_staff WHERE player_uuid='" + target.getUniqueId().toString() + "'");
                        insert.executeUpdate();
                        dm.log("User `" + commandSender.getName() + "` removed a staff member from the database: `" + target.getName() + " (" + target.getUniqueId().toString() + ")" + "`.");
                        commandSender.sendMessage(new ComponentBuilder(Utils.Prefix_Green + "§aSuccessfully removed " + target.getName() + " from staff database.").color(ChatColor.GREEN).create());
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                dm.warning("A SQL exception occurred in **" + this.getClass().getName() + "**, at line **" + new Exception().getStackTrace()[0].getLineNumber() + "**. \nStack trace: `" + e.getMessage() + "` - " + BuildBot.jda.getUserById("161494492422078464").getAsMention());

            }
        }
        if (args[0].equalsIgnoreCase("view")) {
            if (!(commandSender.hasPermission("fadelands.staffmanagement.view"))) {
                commandSender.sendMessage(new ComponentBuilder(Utils.Prefix_Red + "§cYou don't have permission to use that subcommand.").color(ChatColor.RED).create());
                return;
            }
            if(args.length == 1){
                commandSender.sendMessage(new ComponentBuilder(Utils.Prefix + "§cPlease enter a valid user.").color(ChatColor.RED).create());
                return;
            }
            String target = args[1];
            try (Connection connection = Main.getConnection()){
                try(PreparedStatement insert = connection.prepareStatement("SELECT * FROM fadelands_players_staff WHERE player_username='" + target + "'")) {
                    try (ResultSet rs = insert.executeQuery()) {
                        if (!rs.next()) {
                            commandSender.sendMessage(new ComponentBuilder(Utils.Prefix_Red + "§cCouldn't find any staff statistics of that user.").color(ChatColor.RED).create());
                        } else {
                            commandSender.sendMessage(new ComponentBuilder("" +
                                    "§8§l┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓" + "\n" +
                                    "§8§l┣ §2Statistics of staff member §a" + target + "§2.\n" +
                                    "§8§l┣ §3Bans: §b" + rs.getString("bans") + "\n" +
                                    "§8§l┣ §3Kicks: §b" + rs.getString("kicks") + "\n" +
                                    "§8§l┣ §3Mutes: §b" + rs.getString("mutes") + "\n" +
                                    "§8§l┣ §3Handled Reports: §b" + rs.getString("reports_handled") + "\n" +
                                    "§8§l┣ §3Date Hired: §b" + rs.getString("date_hired") + "\n" +
                                    "§8§l┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛").create());
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                dm.warning("A SQL exception occurred in **" + this.getClass().getName() + "**, at line **" + new Exception().getStackTrace()[0].getLineNumber() + "**. \nStack trace: `" + e.getMessage() + "` - " + BuildBot.jda.getUserById("161494492422078464").getAsMention());

            }

        }
    }
}
*/
