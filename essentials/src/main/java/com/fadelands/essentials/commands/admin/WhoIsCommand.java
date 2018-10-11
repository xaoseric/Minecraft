package com.fadelands.essentials.commands.admin;

import com.fadelands.array.Array;
import com.fadelands.essentials.Main;
import com.fadelands.essentials.commands.admin.inventory.WhoisInventory;
import com.fadelands.array.player.User;
import com.fadelands.array.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.*;

public class WhoIsCommand implements CommandExecutor {

    public Main plugin;
    public WhoIsCommand(Main plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cno console");
            return true;
        }
        User user = new User();
        Player player = (Player) sender;
        if(!(user.isAdmin(player.getName()))) {
            player.sendMessage(Utils.No_Perm);
            return true;
        }

        if(args.length == 0){
            player.sendMessage(Utils.Prefix + "§cYou have to enter a username.");
            return true;
        }
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String t = args[0];

        try {
            connection = plugin.getInstance().getDatabaseManager().getConnection();
            ps = connection.prepareStatement("SELECT * FROM players WHERE player_username = ?");
            ps.setString(1, t);
            rs = ps.executeQuery();
            if (!rs.next()) {
                player.sendMessage(Utils.Prefix + "§cCouldn't find that user in the database!");
                return true;
            } else {
                String target = new User().getName(t);
                WhoisInventory inv = new WhoisInventory(plugin);
                inv.whoIs(player, target);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            plugin.getInstance().getDatabaseManager().closeComponents(rs, ps, connection);
        }


        return false;
    }
}