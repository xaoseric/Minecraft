package com.fadelands.core.punishments.commands;

import com.fadelands.core.Core;
import com.fadelands.core.player.UserUtil;
import com.fadelands.core.punishments.Punishment;
import com.fadelands.core.punishments.PunishmentData;
import com.fadelands.core.punishments.PunishmentHandler;
import com.fadelands.core.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class RemoveCommand implements CommandExecutor {

    private Core plugin;
    private PunishmentHandler handler;

    public RemoveCommand(Core plugin, PunishmentHandler handler) {
        this.plugin = plugin;
        this.handler = handler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Utils.No_Console);
            return true;
        }

        Player player = (Player) sender;
        if(!UserUtil.isAdmin(player.getName())) {
            player.sendMessage(Utils.No_Perm);
            return true;
        }

        if(args.length < 2) {
            player.sendMessage(Utils.Prefix + "§cInvalid usage. /removepunish [user] [appealkey] [reason]");
            return true;
        }

        String user = args[0];
        if(!UserUtil.hasPlayedBefore(user)) {
            player.sendMessage(Utils.Prefix + "§cCouldn't find that player.");
            return true;
        }

        String input = args[1];

        UUID uuid = UUID.fromString(UserUtil.getUuid(user));

        PunishmentData.load(uuid, (data) -> {

            if(data == null) {
                return;
            }

            for(Punishment punishment : data.getPunishments()) {
                if(!(input.equals(punishment.getAppealKey()))) {
                    player.sendMessage(Utils.Prefix + "§cThere's no punishment with that appeal key.");
                    return;
                }

                if(punishment.isRemoved()) {
                    player.sendMessage(Utils.Prefix + "§cThat punishment is already removed by " + punishment.getRemoveAdmin() + " with reason \"" + punishment.getRemoveReason() + "\".");
                    return;
                }

                String reason = Arrays.stream(args).skip(2).collect(Collectors.joining(" "));
                System.out.println(reason);

                punishment.remove(player.getUniqueId(), reason);
                handler.removePunishment(input, reason, player.getUniqueId());

                player.sendMessage(Utils.Prefix + "§2Punishment has been removed.");
            }
        });
        return false;
    }
}
