package com.fadelands.core.punishments.commands;

import com.fadelands.core.Core;
import com.fadelands.core.player.UserUtil;
import com.fadelands.core.punishments.Punishment;
import com.fadelands.core.punishments.PunishmentData;
import com.fadelands.core.punishments.PunishmentType;
import com.fadelands.core.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class UnmuteCommand implements CommandExecutor {

    private Core plugin;

    public UnmuteCommand(Core plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Utils.No_Console);
            return true;
        }

        Player player = (Player) sender;
        if(!(UserUtil.isMod(player.getName()))) {
            player.sendMessage(Utils.No_Perm);
            return true;
        }

        if(args.length < 1) {
            player.sendMessage(Utils.Prefix + "§cInvalid usage. /unmute [user] <reason>");
            return true;
        }

        String user = args[0];

        if(!(UserUtil.hasPlayedBefore(user))) {
            player.sendMessage(Utils.Prefix + "§cI couldn't find that player.");
            return true;
        }

        UUID uuid = UUID.fromString(UserUtil.getUuid(user));

        String reason = Arrays.stream(args).skip(0).collect(Collectors.joining(" "));

        PunishmentData.load(uuid, (data) -> {
            if(!(data.hasActive(PunishmentType.Mute))) {
                player.sendMessage(Utils.Prefix + "§cThat user isn't muted.");
                return;
            }

            for(Punishment punishment : data.getActivePunishments(PunishmentType.Mute)) {
                punishment.remove(player.getUniqueId(), reason);
                plugin.getPunishmentHandler().removePunishment(punishment.getAppealKey(), reason, player.getUniqueId());
                Objects.requireNonNull(UserUtil.getOnlineStaff()).sendMessage(Utils.Prefix + "§2" + user + " has been unmuted.");
            }
        });
        return false;
    }
}