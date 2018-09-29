package com.fadelands.array.staff.command;


import com.fadelands.array.Array;
import com.fadelands.array.database.SQLUtils;
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

public class StaffManagementCommand implements CommandExecutor {

    private Array array;

    public StaffManagementCommand(Array array) {
        this.array = array;
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
            ResultSet rs = null;

            try {
                connection = Array.getConnection();
                ps = connection.prepareStatement("SELECT * FROM fadelands_staff_members WHERE player_uuid = ?");
                ps.setString(1, user.getUuid(target));
                rs = ps.executeQuery();
                boolean exists = rs.next();

                if (exists) {
                    player.sendMessage(Utils.Prefix + "§cThat user already exists in the staff database.");
                    return true;
                }
                ps2 = connection.prepareStatement("INSERT INTO " +
                        "fadelands_staff_members " +
                        "(" +
                        "player_uuid," +
                        "date_hired" +
                        ") " +
                        "VALUES (?,?)");

                ps2.setString(1, user.getUuid(target));
                ps2.setTimestamp(2, new Timestamp(new DateTime(DateTimeZone.UTC).getMillis()));
                ps2.executeUpdate();
                ps2.close();
                player.sendMessage(Utils.Prefix + "§aSuccessfully added " + target + " to staff database.");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                Array.closeComponents(rs, ps, connection);
                Array.closeComponents(ps2);
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
                connection = Array.getConnection();
                ps = connection.prepareStatement("SELECT * FROM fadelands_staff_members WHERE player_uuid = ?");
                ps.setString(1, user.getUuid(target));
                rs = ps.executeQuery();
                boolean exists = rs.next();
                if (!exists) {
                    player.sendMessage(Utils.Prefix + "§cThere is no user with that name in the staff database.");
                    return true;
                }

                SQLUtils.updateTable(target, "fadelands_staff_members", "resignation_date", new Timestamp(new DateTime(DateTimeZone.UTC).getMillis()));
                player.sendMessage(Utils.Prefix + "§aAdded resignation date to " + target + " in staff database.");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                Array.closeComponents(rs, ps, connection);
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
                connection = Array.getConnection();
                ps = connection.prepareStatement("SELECT * FROM fadelands_staff_members WHERE player_uuid = ?");
                ps.setString(1, user.getUuid(target));
                rs = ps.executeQuery();
                if (!rs.next()) {
                    player.sendMessage(Utils.Prefix + "§cCouldn't find any staff statistics of that user.");
                } else {
                    StaffInventory inv = new StaffInventory(array);
                    inv.openInventory(player, target);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                Array.closeComponents(rs,ps,connection);
            }
        }
        return false;
    }
}