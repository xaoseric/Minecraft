package com.fadelands.core.punishments.commands;

import com.fadelands.core.Core;
import com.fadelands.core.player.UserUtil;
import com.fadelands.core.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class AltsCommand implements CommandExecutor {

    private Core plugin;

    public AltsCommand(Core plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Utils.No_Console);
            return true;
        }
        Player player = (Player) sender;
        if(!(UserUtil.isSenior(player.getName()))) {
            sender.sendMessage(Utils.No_Perm);
            return true;
        }

        if(args.length == 0){
            sender.sendMessage(Utils.Prefix_Red + "§cPlease enter a username.");
            return true;
        }

        String targetRaw = args[0];
        if(!(UserUtil.hasPlayedBefore(targetRaw))){
            sender.sendMessage(Utils.Prefix_Red + "§cI couldn't find that user.");
            return true;
        }

        String ip = UserUtil.getIp(targetRaw);
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String query = "SELECT * FROM players WHERE first_ip = ? OR last_ip LIKE ?";
            connection = plugin.getDatabaseManager().getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, ip);
            ps.setString(2, ip);
            rs = ps.executeQuery();
            if(!rs.next()){
                sender.sendMessage(Utils.Prefix + "Couldn't get the IP address of that user in the database.");
            }else{
                List<String> usernames = new LinkedList<>();
                do {
                    usernames.add(rs.getString("player_username"));
                } while (rs.next());
                sender.sendMessage("§6Following accounts are sharing IP with the target user: ");
                sender.sendMessage("§2" + String.join(", ", usernames));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            plugin.getDatabaseManager().closeComponents(rs, ps, connection);
        }
        return false;
    }
}
