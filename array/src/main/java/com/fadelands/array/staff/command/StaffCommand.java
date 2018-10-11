package com.fadelands.array.staff.command;


import com.fadelands.array.Array;
import com.fadelands.array.player.User;
import com.fadelands.array.staff.inventory.StaffInventory;
import com.fadelands.array.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.*;

public class StaffCommand implements CommandExecutor {

    private Array plugin;

    public StaffCommand(Array plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        User user = new User();
        if (!(user.isAdmin(player.getName()))) {
            player.sendMessage(Utils.No_Perm);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Utils.Prefix + "§cInvalid usage. /staff <add/remove/view> <target>");
            return true;
        }

        if (args[0].equalsIgnoreCase("add")) {
            if (args.length == 1) {
                player.sendMessage(Utils.Prefix + "§cPlease enter a valid user.");
                return true;
            }

            String target = args[1];
            if (!user.hasPlayedBefore(target)) {
                player.sendMessage(Utils.Prefix + "§cThat's not a valid user.");
                return true;
            }

            Connection connection = null;
            PreparedStatement ps = null;
            PreparedStatement ps2 = null;
            PreparedStatement ps3 = null;
            ResultSet rs = null;

            try {
                connection = plugin.getDatabaseManager().getConnection();
                ps = connection.prepareStatement("SELECT * FROM staff_members WHERE player_uuid = ?");
                ps.setString(1, user.getUuid(target));
                rs = ps.executeQuery();
                boolean exists = rs.next();

                if (exists) {
                    player.sendMessage(Utils.Prefix + "§cThat user already exists in the staff database.");
                    return true;
                }

                ps2 = connection.prepareStatement("INSERT INTO " +
                        "staff_members " +
                        "(" +
                        "player_uuid," +
                        "date_hired" +
                        ") " +
                        "VALUES (?,?)");

                ps2.setString(1, user.getUuid(target));
                ps2.setTimestamp(2, new Timestamp(new DateTime(DateTimeZone.UTC).getMillis()));
                ps2.executeUpdate();

                ps3 = connection.prepareStatement("INSERT INTO staff_settings (player_uuid) VALUES (?)");
                ps3.setString(1, user.getUuid(target));
                ps3.executeUpdate();

                player.sendMessage(Utils.Prefix + "§aSuccessfully added " + target + " to staff database.");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                plugin.getDatabaseManager().closeComponents(rs, ps, connection);
                plugin.getDatabaseManager().closeComponents(ps2);
                plugin.getDatabaseManager().closeComponents(ps3);
            }
        }

        if (args[0].equalsIgnoreCase("remove")) { // add resignation methods
            if (args.length == 1) {
                player.sendMessage(Utils.Prefix + "§cPlease enter a valid user.");
                return true;
            }

            String target = args[1];
            if (!user.hasPlayedBefore(target)) {
                player.sendMessage(Utils.Prefix + "§cThat's not a valid user.");
                return true;
            }

            Connection connection = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                connection = plugin.getDatabaseManager().getConnection();
                ps = connection.prepareStatement("SELECT * FROM staff_members WHERE player_uuid = ?");
                ps.setString(1, user.getUuid(target));
                rs = ps.executeQuery();
                boolean exists = rs.next();
                if (!exists) {
                    player.sendMessage(Utils.Prefix + "§cThere is no user with that name in the staff database.");
                    return true;
                }

                plugin.getDatabaseManager().updateTable(target, "staff_members", "resignation_date", new Timestamp(new DateTime(DateTimeZone.UTC).getMillis()));
                player.sendMessage(Utils.Prefix + "§aAdded resignation date to " + target + " in staff database.");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                plugin.getDatabaseManager().closeComponents(rs, ps, connection);
            }
        }

        if (args[0].equalsIgnoreCase("view")) {
            if (args.length == 1) {
                player.sendMessage(Utils.Prefix + "§cPlease enter a valid user.");
                return true;
            }

            String target = args[1];
            if (!user.hasPlayedBefore(target)) {
                player.sendMessage(Utils.Prefix + "§cThat's not a valid user.");
                return true;
            }

            Connection connection = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {
                connection = plugin.getDatabaseManager().getConnection();
                ps = connection.prepareStatement("SELECT * FROM staff_members WHERE player_uuid = ?");
                ps.setString(1, user.getUuid(target));
                rs = ps.executeQuery();
                if (!rs.next()) {
                    player.sendMessage(Utils.Prefix + "§cCouldn't find any staff statistics of that user.");
                } else {
                    StaffInventory inv = new StaffInventory(plugin);
                    inv.openInventory(player, target);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                plugin.getDatabaseManager().closeComponents(rs,ps,connection);
            }
        }
        return false;
    }
}
