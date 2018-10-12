package com.fadelands.core.achievements;

import com.fadelands.core.Core;
import com.fadelands.core.player.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("Duplicates")
public class AchievementCommand implements CommandExecutor {

    public Core plugin;

    public AchievementCommand(Core plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used in-game.");
            return true;
        }
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("achievements")) {
            if (args.length == 0) {
                AchievementGui.openPersonalAchievements(player);
                return false;
            }

            String targetStr = args[0];

            if(!(new User().hasPlayedBefore(targetStr))) {
                            player.sendMessage("§cThat player has never played on the server before.");
                        } else {
                            AchievementGui.openOtherPlayerAchievements(player, targetStr);
                        }
            }
            return false;
        }
    }
