package com.fadelands.core.achievement;

import com.fadelands.array.Array;
import com.fadelands.core.CorePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

@SuppressWarnings("Duplicates")
public class AchievementCommand implements CommandExecutor {

    public CorePlugin plugin;

    public AchievementCommand(CorePlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used in-game.");
            return true;
        }
        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("achievement")) {
            if (args.length == 0) {
                AchievementGui.openPersonalAchievements(player);
                return false;
            }

            String targetStr = args[0];

            Array.executeQuery("SELECT * FROM fadelands_players WHERE player_username='" + targetStr + "'", rs -> {
                    try {
                        if (!rs.next()) {
                            player.sendMessage("§cThat player has never played on the server before.");
                        } else {
                            AchievementGui.openOtherPlayerAchievements(player, targetStr);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }
            return false;
        }
    }
