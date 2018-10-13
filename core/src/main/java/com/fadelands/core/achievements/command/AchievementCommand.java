package com.fadelands.core.achievements.command;

import com.fadelands.core.Core;
import com.fadelands.core.achievements.inventory.AchievementGui;
import com.fadelands.core.player.User;
import com.fadelands.core.utils.Utils;
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
            sender.sendMessage(Utils.No_Console);
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            new AchievementGui().openAchievements(player, player.getName());
            return false;
            }

            String targetStr = args[0];

            if (!(User.hasPlayedBefore(targetStr))) {
                player.sendMessage(Utils.Prefix + "§cI couldn't find that player.");
            } else {
                new AchievementGui().openAchievements(player, targetStr);
            }

        return false;
    }
}
