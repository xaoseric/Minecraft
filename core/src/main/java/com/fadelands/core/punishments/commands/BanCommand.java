package com.fadelands.core.punishments.commands;

import com.fadelands.core.Core;
import com.fadelands.core.punishments.PunishmentManager;
import com.fadelands.core.player.UserUtil;
import com.fadelands.core.punishments.PunishmentType;
import com.fadelands.core.utils.Utils;
import com.google.common.base.Joiner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class BanCommand implements CommandExecutor {

    private PunishmentManager punishmentManager;

    public BanCommand(Core core, PunishmentManager punishmentManager){
        this.punishmentManager = punishmentManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!UserUtil.isMod(sender.getName())) {
            sender.sendMessage(Utils.No_Perm);
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(Utils.Prefix_Red + "§cInvalid usage. /ban <user> [time] <reason>");
            return true;
        }

        String playerName = args[0];
        UUID playerUuid = UUID.fromString(UserUtil.getUuid(playerName));
        if(!(UserUtil.hasPlayedBefore(playerName))){
            sender.sendMessage(Utils.Prefix_Red + "§cThat's not a valid player.");
            return true;
        }

        String reason;
        boolean permanent;
        if (punishmentManager.getTimeToPunish(args, sender) == 0) {
            reason = Joiner.on(" ").skipNulls().join(Arrays.copyOfRange(args, 1, args.length));
            permanent = true;
        } else {
            reason = Joiner.on(" ").skipNulls().join(Arrays.copyOfRange(args, 2, args.length));
            permanent = false;
        }

        if (!(sender instanceof Player)) {
            punishmentManager.addPunishment(sender, playerName, playerUuid, reason, UUID.fromString("Console"), PunishmentType.Ban, punishmentManager.getTimeToPunish(args, sender), permanent);
        } else {
            Player caller = (Player) sender;
            punishmentManager.addPunishment(sender, playerName, playerUuid, reason, caller.getUniqueId(), PunishmentType.Ban, punishmentManager.getTimeToPunish(args, sender), permanent);
        }
        return false;
    }
}
