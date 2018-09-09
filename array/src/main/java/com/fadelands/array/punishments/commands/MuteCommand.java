package com.fadelands.array.punishments.commands;

import com.fadelands.array.Array;
import com.fadelands.array.punishments.PunishmentManager;
import com.fadelands.array.player.User;
import com.fadelands.array.utils.Utils;
import com.google.common.base.Joiner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MuteCommand implements CommandExecutor {

    private Array array;
    private PunishmentManager punishmentManager;

    public MuteCommand(Array array, PunishmentManager punishmentManager){
        this.array = array;
        this.punishmentManager = punishmentManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cNot usable in console");
            return true;
        }

        User user = new User();
        Player player = (Player) sender;
        if(!user.isStaff(player.getName())) {
            sender.sendMessage(Utils.No_Perm);
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(Utils.Prefix_Red + "§cInvalid usage. /mute <user> [time] <reason>");
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
            punishmentManager.addPunishment(sender, playerName, playerUuid, reason, "Console", false, punishmentManager.getTimeToPunish(args, sender));
        } else {
            Player caller = (Player) sender;
            punishmentManager.addPunishment(sender, playerName, playerUuid, reason, caller.getUniqueId().toString(), false, punishmentManager.getTimeToPunish(args, sender));
        }
        return false;
    }
}

