package com.fadelands.core.punishments.commands;

import com.fadelands.core.player.User;
import com.fadelands.core.punishments.Punishment;
import com.fadelands.core.punishments.PunishmentData;
import com.fadelands.core.punishments.PunishmentType;
import com.fadelands.core.utils.UtilTime;
import com.fadelands.core.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CheckBanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Utils.No_Console);
            return true;
        }

        Player player = (Player) sender;
        if(!(User.isMod(player.getName()))) {
            player.sendMessage(Utils.No_Perm);
            return true;
        }

        if(args.length == 0) {
            player.sendMessage(Utils.Prefix + "§cInvalid usage. /checkban <user>");
            return true;
        }

        String user = args[0];

        if(!(User.hasPlayedBefore(user))) {
            player.sendMessage(Utils.Prefix + "§cI couldn't find that player.");
            return true;
        }

        UUID uuid = UUID.fromString(User.getUuid(user));

        PunishmentData.load(uuid, (data) -> {
            if(!data.hasActive(PunishmentType.Ban)) {
                player.sendMessage(Utils.Prefix + "§cThat user has no active ban.");
                return;
            }

            for(Punishment punishment : data.getPunishments(PunishmentType.Ban)) {
                player.sendMessage("§6Found an active ban of user " + User.getName(user) + ". \n" +
                        "§fIssued On: §a" + UtilTime.when(punishment.getPunishTime()) + "\n" +
                        "§fExpires in: §a" + (punishment.isPermanent() ? "Never (Permanent)" : UtilTime.MakeStr(punishment.getExpirationTime())) + "\n" +
                        "§fPunisher: §a" + User.getNameFromUuid(String.valueOf(punishment.getPunisherUuid())) + "\n" +
                        "§fReason: §a" + punishment.getReason() + "\n" +
                        "§fAppeal Key: §a" + punishment.getAppealKey());
            }
        });

        return false;
    }
}
