package com.fadelands.array.commands.admin;

import com.fadelands.array.Array;
import com.fadelands.array.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LockdownCommandExecutor implements CommandExecutor {

    private Array array;

    public LockdownCommandExecutor(Array array){
        this.array = array;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("ik this should be allowed in console but console has no uuids :( use it ingame");
            return true;
        }
        Player player = (Player) sender;
        if (!(player.hasPermission("fadelands.lockdown"))) {
            sender.sendMessage(Utils.No_Perm);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Utils.Prefix + "§cInvalid usage. /lockdown <reason> or /lockdown off.");
            return true;
        }

        if (args[0].equalsIgnoreCase("off")) {
            if (!(array.getServerManager().lockdownActive())) {
                player.sendMessage(Utils.Prefix + "§cThere's no active lockdown.");
                return true;
            }
                array.getServerManager().disableLockdown();
                player.sendMessage(Utils.Prefix + "§2Disabled server lockdown.");
                return true;
            }

            if (array.getServerManager().lockdownActive()) {
                player.sendMessage(Utils.Prefix + "§cThere's already a lockdown active.");
                return true;
            }

            String reason = String.join(" ", args);
            array.getServerManager().activateLockdown(player, reason);
            player.sendMessage(Utils.Prefix + "§2Activated network lockdown with reason §a" + reason + "§2.");

        return false;
    }
}
