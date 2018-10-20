package com.fadelands.essentials.warps.command;

import com.fadelands.core.player.UserUtil;
import com.fadelands.core.utils.Utils;
import com.fadelands.essentials.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddWarpCommand implements CommandExecutor {

    private Main plugin;

    public AddWarpCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Utils.No_Console);
            return true;
        }

        Player player = (Player) sender;
        if(!UserUtil.isAdmin(player.getName())) {
            player.sendMessage(Utils.No_Perm);
            return true;
        }

        if(args.length == 0) {
            player.sendMessage(Utils.Prefix + "§cInvalid usage. /addwarp [name]");
            return true;
        }

        String name = args[0];

        if(plugin.getWarps().warpExists(name)) {
            player.sendMessage(Utils.Prefix + "§cA warp with that name already exists.");
            return true;
        }

        plugin.getWarps().addWarp(player.getLocation(), name);

        player.sendMessage(Utils.Prefix + "§2Added warp with name " + name + ".");

        return false;
    }
}
