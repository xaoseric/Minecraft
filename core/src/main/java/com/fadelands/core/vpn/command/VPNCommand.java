package com.fadelands.core.vpn.command;

import com.fadelands.core.Core;
import com.fadelands.core.player.UserUtil;
import com.fadelands.core.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VPNCommand implements CommandExecutor {

    private Core plugin;

    public VPNCommand(Core plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Utils.No_Console);
            return true;
        }

        Player player = (Player) sender;
        if(!(UserUtil.isAdmin(player.getName()))) {
            player.sendMessage(Utils.No_Perm);
            return true;
        }

        if(args.length == 0) {
            player.sendMessage(Utils.Prefix + "§cInvalid usage. /vpn <add/remove/wave>");
            return true;
        }

        if(args[0].equalsIgnoreCase("add")) {
            if(args.length == 1) {
                player.sendMessage(Utils.Prefix + "§cYou need to enter an IP address.");
                return true;
            }

            String ip = args[1];
            if(!(plugin.getVpnManager().validIpAddress(ip))) {
                player.sendMessage(Utils.Prefix + "§cThat's not a valid IP.");
                return true;
            }

            if(plugin.getVpnManager().ipBlocked(ip)) {
                player.sendMessage(Utils.Prefix + "§cThat IP is already blocked.");
                return true;
            }

            plugin.getVpnManager().blockIp(ip);
            player.sendMessage(Utils.Prefix + "§aIP has been added to the database.");
        }

        if(args[0].equalsIgnoreCase("remove")) {
            if(args.length == 1) {
                player.sendMessage(Utils.Prefix + "§cYou need to enter an IP address.");
                return true;
            }

            String ip = args[1];
            if(!(plugin.getVpnManager().validIpAddress(ip))) {
                player.sendMessage(Utils.Prefix + "§cThat's not a valid IP.");
                return true;
            }

            if(!(plugin.getVpnManager().ipBlocked(ip))) {
                player.sendMessage(Utils.Prefix + "§cThat IP is not in the database.");
                return true;
            }

            plugin.getVpnManager().unblockIp(ip);
            player.sendMessage(Utils.Prefix + "§aIP removed from database.");
        }

        /*if(args[0].equalsIgnoreCase("wave")) {
            plugin.getVpnManager().addIps();
            player.sendMessage("§aAdded: ");
            System.out.println("§aAdded following ips: ");
            for(String ips : plugin.getVpnManager().ips) {
                player.sendMessage(ips);
                System.out.println(ips);
                plugin.getVpnManager().blockIp(ips);
            }
        }*/
        return false;
    }
}
