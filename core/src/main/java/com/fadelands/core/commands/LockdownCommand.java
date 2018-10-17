package com.fadelands.core.commands;

import com.fadelands.core.Core;
import com.fadelands.core.player.UserUtil;
import com.fadelands.core.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LockdownCommand implements CommandExecutor {

    private Core core;

    public LockdownCommand(Core core){
        this.core = core;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("ik this should be allowed in console but console has no uuids :( use it ingame");
            return true;
        }
        Player player = (Player) sender;
        if(!(UserUtil.isAdmin(player.getName()))) {
            sender.sendMessage(Utils.No_Perm);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Utils.Prefix + "§cInvalid usage. /lockdown <reason> or /lockdown off.");
            if(core.getServerManager().lockdownActive()){
                player.sendMessage("§4Lockdown Enabled: §c" + core.getServerManager().getLockdownReason());
            }
            else{
                player.sendMessage("§aLockdown currently disabled.");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("off")) {
            if (!(core.getServerManager().lockdownActive())) {
                player.sendMessage(Utils.Prefix + "§cThere's no active lockdown.");
                return true;
            }
                core.getServerManager().disableLockdown();
                player.sendMessage(Utils.Prefix + "§2Disabled server lockdown.");
                core.getPluginMessage().sendMessageToAll(player, "\n§r\n§r                          §c§lLockdown§7§r\n" +
                    "     §6FadeLands Network is no longer in lockdown mode. \n" +
                    "§r");
                return true;
            }

            if (core.getServerManager().lockdownActive()) {
                player.sendMessage(Utils.Prefix + "§cThere's already a lockdown active.");
                return true;
            }

            String reason = String.join(" ", args);
            core.getServerManager().activateLockdown(player, reason);
            player.sendMessage(Utils.Prefix + "§2Activated network lockdown with reason §a" + reason + "§2.");
            core.getPluginMessage().sendMessageToAll(player, "\n§r\n§r                           §c§lLockdown§7§r\n" +
                    "     §6FadeLands Network has went into lockdown mode. \n" +
                    "           §7Please log out as soon as possible.\n§r");

        return false;
    }
}
