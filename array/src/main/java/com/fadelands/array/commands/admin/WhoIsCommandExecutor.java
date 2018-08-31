package com.fadelands.array.commands.admin;

import com.fadelands.array.Array;
import com.fadelands.array.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WhoIsCommandExecutor implements CommandExecutor {

    public Array plugin;
    public WhoIsCommandExecutor(Array plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(!(sender.isOp()) || (!(sender.hasPermission("fadelands.whois")))){
            sender.sendMessage(Utils.No_Perm);
            return true;
        }

        if(args.length == 0){
            sender.sendMessage("§cYou have to enter a username.");
            return true;
        }
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        PreparedStatement rank = null;
        ResultSet rankRs = null;

        String user = args[0];


        try {
            connection = Array.getConnection();
            ps = connection.prepareStatement("SELECT * FROM fadelands_players WHERE player_username = ?");
            ps.setString(1, user);
            rs = ps.executeQuery();
            if (!rs.next()) {
                sender.sendMessage("§cCouldn't find that user in the database!");
                return true;
            } else {

                rank = connection.prepareStatement("SELECT * FROM luckperms_players WHERE username = ?");
                rank.setString(1, user);

                rankRs = rank.executeQuery();

                if (!(rankRs.next())) {
                    sender.sendMessage("§cCouldn't get the user group of the player.");
                    return true;
                } else {

                    DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Timestamp firstJoinRaw = new Timestamp(rs.getTimestamp("first_join").getTime());

                    Date firstJoin = new Date(firstJoinRaw.getTime());
                    Date lastLogin = null;

                        if(rs.getTimestamp("last_login") != null) {
                            Timestamp lastLoginRaw = new Timestamp(rs.getTimestamp("last_login").getTime());
                            lastLogin = new Date(lastLoginRaw.getTime());
                        }
                        sender.sendMessage("§8§l§m|--§8§l» §2§lWho Is: §f" + user + " §8§l«§m--|");
                        sender.sendMessage("§8§l» §a§lFirst join§f: " + f.format(firstJoin));
                        sender.sendMessage("§8§l» §a§lLast seen§f: " + (lastLogin == null ? "User only logged in once." : f.format(lastLogin)));
                        sender.sendMessage("§8§l» §a§lIP§f: " + rs.getString("last_ip"));
                        sender.sendMessage("§8§l» §a§lCountry§f: " + rs.getString("last_country"));
                        sender.sendMessage("§8§l» §a§lRank§f: " + rankRs.getString("primary_group").toUpperCase());
                        sender.sendMessage("§8§l» §a§lReported§f: " + rs.getInt("times_reported") + " times");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            Array.closeComponents(rs, ps, connection);
            Array.closeComponents(rank);
            Array.closeComponents(rankRs);
        }


        return false;
    }
}
