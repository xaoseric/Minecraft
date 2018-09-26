package com.fadelands.array.punishments.commands;

import com.fadelands.array.Array;
import com.fadelands.array.player.User;
import com.fadelands.array.utils.Utils;
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

    private Array array;

    public AltsCommand(Array array){
        this.array = array;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cOnly ingame.");
            return true;
        }
        Player player = (Player) sender;
        if(!(new User().isSenior(player.getName()))) {
            sender.sendMessage(Utils.No_Perm);
            return true;
        }

        if(args.length == 0){
            sender.sendMessage(Utils.Prefix_Red + "§cPlease enter a username.");
            return true;
        }

        String targetRaw = args[0];
        User user = new User();
        if(!(user.hasPlayedBefore(targetRaw))){
            sender.sendMessage(Utils.Prefix_Red + "§cI couldn't find that user.");
            return true;
        }

        String ip = user.getIp(targetRaw);
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String query = "SELECT * FROM fadelands_players WHERE first_ip = ? OR last_ip LIKE ?";
            connection = Array.getConnection();
            ps = connection.prepareStatement(query);
            ps.setString(1, ip);
            ps.setString(2, ip);
            rs = ps.executeQuery();
            if(!rs.next()){
                sender.sendMessage(Utils.Prefix_Red + "Couldn't get the IP address of that user in the database.");
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
            Array.closeComponents(rs, ps, connection);
        }
        return false;
    }
}
