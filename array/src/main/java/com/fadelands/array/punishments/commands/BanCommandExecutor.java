package com.fadelands.array.punishments.commands;

import com.fadelands.array.Array;
import com.fadelands.array.punishments.PunishmentManager;
import com.fadelands.array.players.User;
import com.fadelands.array.utils.Utils;
import com.google.common.base.Joiner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class BanCommandExecutor implements CommandExecutor {

    private Array array;
    private PunishmentManager punishmentManager;

    public BanCommandExecutor(Array array, PunishmentManager punishmentManager){
        this.array = array;
        this.punishmentManager = punishmentManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender.hasPermission("fadelands.punish.ban"))) {
            sender.sendMessage(Utils.No_Perm);
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(Utils.Prefix_Red + "§cInvalid usage. /ban <user> [time] <reason>");
            return true;
        }
        User fadeLandsPlayer = new User();
        String playerName = args[0];
        String playerUuid = fadeLandsPlayer.getUuid(playerName);
        if(!(fadeLandsPlayer.hasPlayedBefore(playerName))){
            sender.sendMessage(Utils.Prefix_Red + "§cThat's not a valid player.");
            return true;
        }

        String reason;
        if (punishmentManager.getTimeToPunish(args, sender) == 0) {
            reason = Joiner.on(" ").skipNulls().join(Arrays.copyOfRange(args, 1, args.length));
        } else {
            reason = Joiner.on(" ").skipNulls().join(Arrays.copyOfRange(args, 2, args.length));
        }
        if (!(sender instanceof Player)) {
            punishmentManager.addPunishment(sender, playerName, playerUuid, reason, "Console", true, punishmentManager.getTimeToPunish(args, sender));
        } else {
            Player caller = (Player) sender;
            punishmentManager.addPunishment(sender, playerName, playerUuid, reason, caller.getUniqueId().toString(), true, punishmentManager.getTimeToPunish(args, sender));
        }
        return false;
    }
}
