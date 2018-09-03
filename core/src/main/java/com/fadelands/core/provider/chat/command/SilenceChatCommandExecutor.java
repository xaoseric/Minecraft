package com.fadelands.core.provider.chat.command;

import com.fadelands.array.utils.TimeUtils;
import com.fadelands.array.utils.Utils;
import com.fadelands.core.CorePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SilenceChatCommandExecutor implements CommandExecutor {

    private CorePlugin plugin;

    public SilenceChatCommandExecutor(CorePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cThis command can only be used ingame.");
            return true;
        }

        Player player = (Player) sender;
        if (!(player.hasPermission("fadelands.silencechat"))) {
            player.sendMessage(Utils.No_Perm);
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(Utils.Prefix + "§cInvalid usage. /silencechat [time] (0 for perma) or \"/silencechat off\" to disable.");
            return true;
        }

        if(args[0].equalsIgnoreCase("off")) {
            plugin.getServerChat().silenceEnd();
            player.sendMessage(Utils.Prefix + "§cDisabled Chat Silence.");
            return true;
        }

        long time = getSilenceTime(args, sender);

        plugin.getServerChat().setSilence(time, true);
        player.sendMessage(Utils.Prefix + "§aSilenced chat for " + TimeUtils.toRelative(time) + ".");

        return false;
    }

    @SuppressWarnings("Duplicates")
    public long getSilenceTime(String[] args, CommandSender sender) {
        if (args[0].substring(0, 1).matches("[0-9]")) {
            try {
                char timeLength = args[0].charAt(args[0].length() - 1);
                long time = Long.parseLong(args[0].substring(0, args[0].length() - 1));

                if (timeLength == 's' || timeLength == 'S') {
                    time = time * 1000;
                } else if (timeLength == 'm' || timeLength == 'M') {
                    time = time * 1000 * 60;
                } else if (timeLength == 'h' || timeLength == 'H') {
                    time = time * 1000 * 60 * 60;
                } else if (timeLength == 'd' || timeLength == 'D') {
                    time = time * 1000 * 60 * 60 * 24;
                } else if (timeLength == 'w' || timeLength == 'W') {
                    time = time * 1000 * 60 * 60 * 24 * 7;
                } else if (timeLength == 'n' || timeLength == 'N') {
                    time = time * 1000 * 60 * 60 * 24 * 30;
                } else if (timeLength == 'y' || timeLength == 'Y') {
                    time = time * 1000 * 60 * 60 * 24 * 365;
                } else {
                    sender.sendMessage(Utils.Prefix_Red + "§cNot a valid date format. (#s/m/h/d/w/n)");
                    return -30;
                }
                return time;
            } catch (Exception e) {
                sender.sendMessage(Utils.Prefix_Red + "§c" + args[0] + " is an invalid date format.");
                return -30;
            }
        }
        return 0;
    }
}
