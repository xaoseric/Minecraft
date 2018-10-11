package com.fadelands.array.commands;

import com.fadelands.array.Array;
import com.fadelands.array.player.User;
import com.fadelands.array.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LockdownCommand implements CommandExecutor {

    private Array array;

    public LockdownCommand(Array array){
        this.array = array;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("ik this should be allowed in console but console has no uuids :( use it ingame");
            return true;
        }
        Player player = (Player) sender;
        User user = new User();
        if(!(user.isAdmin(player.getName()))) {
            sender.sendMessage(Utils.No_Perm);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Utils.Prefix + "§cInvalid usage. /lockdown <reason> or /lockdown off.");
            if(array.getServerManager().lockdownActive()){
                player.sendMessage("§4Lockdown Enabled: §c" + array.getServerManager().getLockdownReason());
            }
            else{
                player.sendMessage("§aLockdown currently disabled.");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("off")) {
            if (!(array.getServerManager().lockdownActive())) {
                player.sendMessage(Utils.Prefix + "§cThere's no active lockdown.");
                return true;
            }
                array.getServerManager().disableLockdown();
                player.sendMessage(Utils.Prefix + "§2Disabled server lockdown.");
                array.getPluginMessage().sendMessageToAll(player, "\n§r\n§r                          §c§lLockdown§7§r\n" +
                    "     §6FadeLands Network is no longer in lockdown mode. \n" +
                    "§r");
                return true;
            }

            if (array.getServerManager().lockdownActive()) {
                player.sendMessage(Utils.Prefix + "§cThere's already a lockdown active.");
                return true;
            }

            String reason = String.join(" ", args);
            array.getServerManager().activateLockdown(player, reason);
            player.sendMessage(Utils.Prefix + "§2Activated network lockdown with reason §a" + reason + "§2.");
            array.getPluginMessage().sendMessageToAll(player, "\n§r\n§r                           §c§lLockdown§7§r\n" +
                    "     §6FadeLands Network has went into lockdown mode. \n" +
                    "           §7Please log out as soon as possible.\n§r");

        return false;
    }
}
