package com.fadelands.core.commands.amodcmds;

import com.fadelands.core.CorePlugin;
import com.fadelands.array.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommandExecutor implements CommandExecutor {

    public CorePlugin plugin;
    public TeleportCommandExecutor(CorePlugin plugin)
    {
        this.plugin = plugin;
    }
    

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        Player player = (Player)sender;

            if ((player.hasPermission("fadelands.tp")) && (args.length == 2)) {
                if (Bukkit.getPlayerExact(args[0]) != null && Bukkit.getPlayerExact(args[1]) != null)
                {
                    Player targetPlayer = player.getServer().getPlayer(args[0]);
                    Player targetPlayer1 = player.getServer().getPlayer(args[1]);
                    Location targetPlayer1Location = targetPlayer1.getLocation();
                    targetPlayer.teleport(targetPlayer1Location);

                    return true;
                }
                player.sendMessage("§cCouldn't find the player(s).");
                return true;
            }
            if (args.length == 2) {
                player.sendMessage(Utils.No_Perm);
            }
            if ((args.length == 1) || (args.length == 0)) {
                if (player.hasPermission("fadelands.tp"))
                {
                    if (args.length == 0)
                    {
                        player.sendMessage("§cYou have to specify a player.");
                    }
                    else
                    {
                        if (Bukkit.getPlayerExact(args[0]) != null)
                        {
                            Player targetPlayer = player.getServer().getPlayer(args[0]);
                            Location targetPlayerLocation = targetPlayer.getLocation();
                            player.teleport(targetPlayerLocation);

                            return true;
                        }
                        player.sendMessage("§cCould't find the player.");
                        return true;
                    }
                }
                else {
                    player.sendMessage(Utils.No_Perm);
                }
            }

        return false;
    }
}


