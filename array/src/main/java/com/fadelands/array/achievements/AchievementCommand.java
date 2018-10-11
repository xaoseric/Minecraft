package com.fadelands.array.achievements;

import com.fadelands.array.Array;
import com.fadelands.array.player.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

@SuppressWarnings("Duplicates")
public class AchievementCommand implements CommandExecutor {

    public Array plugin;

    public AchievementCommand(Array plugin) {
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
