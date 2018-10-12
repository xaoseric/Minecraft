package com.fadelands.essentials.commands.admin;

import com.fadelands.essentials.Main;
import com.fadelands.essentials.commands.admin.inventory.WhoisInventory;
import com.fadelands.core.player.User;
import com.fadelands.core.utils.Utils;
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
            sender.sendMessage(Utils.No_Console);
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

        String t = args[0];

        if(!(new User().hasPlayedBefore(t))) {
                player.sendMessage(Utils.Prefix + "§cCouldn't find that user in the database.");
                return true;
            } else {
            String target = new User().getName(t);
            WhoisInventory inv = new WhoisInventory(plugin);
            inv.whoIs(player, target);
        }
        return false;
    }
}
